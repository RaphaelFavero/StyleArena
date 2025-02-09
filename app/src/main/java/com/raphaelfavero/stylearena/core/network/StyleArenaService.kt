package com.raphaelfavero.stylearena.core.network


import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import retrofit2.http.Url

interface StyleArenaService {

    @GET("getUploadUrl")
    suspend fun getUploadUrl(@Query("session_id") sessionId: String): Response<UploadURLResponse>

    @POST
    @Multipart
    suspend fun uploadImage(
        @Url s3Url: String,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @GET("getStylePair")
    suspend fun getStylePair(@Query("session_id") sessionId: String): Response<StylePairResponse>

    @GET("getDailyWinners")
    suspend fun getDailyWinners(@Query("session_id") sessionId: String): Response<DailyWinnersResponse>

    @PUT("voteForStyle")
    suspend fun voteForStyle(@Body() vote: VoteRequest): Response<Unit>

}

data class VoteRequest(
    @SerializedName("vote_id") val voteId: String,
    @SerializedName("session_id") val sessionId: String
)

data class UploadURLResponse(val url: String, val fields: S3PostFields)

data class StylePairResponse(
    @SerializedName("style_1")
    val style1: Style,
    @SerializedName("style_2")
    val style2: Style
)

data class DailyWinnersResponse(
    @SerializedName("rank_1")
    val rank1: Style,
    @SerializedName("rank_2")
    val rank2: Style,
    @SerializedName("rank_3")
    val rank3: Style,
)

data class Style(val url: String, val id: String)

data class S3PostFields(
    @SerializedName("key")
    val key: String,

    @SerializedName("x-amz-algorithm")
    val xAmzAlgorithm: String,

    @SerializedName("x-amz-credential")
    val xAmzCredential: String,

    @SerializedName("x-amz-date")
    val xAmzDate: String,

    @SerializedName("x-amz-security-token")
    val xAmzSecurityToken: String,

    @SerializedName("policy")
    val policy: String,

    @SerializedName("x-amz-signature")
    val xAmzSignature: String
)

fun S3PostFields.toPartMap(): Map<String, RequestBody> {
    return mapOf(
        "key" to key.toRequestBody("text/plain".toMediaTypeOrNull()),
        "x-amz-algorithm" to xAmzAlgorithm.toRequestBody("text/plain".toMediaTypeOrNull()),
        "x-amz-credential" to xAmzCredential.toRequestBody("text/plain".toMediaTypeOrNull()),
        "x-amz-date" to xAmzDate.toRequestBody("text/plain".toMediaTypeOrNull()),
        "x-amz-security-token" to xAmzSecurityToken.toRequestBody("text/plain".toMediaTypeOrNull()),
        "policy" to policy.toRequestBody("text/plain".toMediaTypeOrNull()),
        "x-amz-signature" to xAmzSignature.toRequestBody("text/plain".toMediaTypeOrNull())
    )
}
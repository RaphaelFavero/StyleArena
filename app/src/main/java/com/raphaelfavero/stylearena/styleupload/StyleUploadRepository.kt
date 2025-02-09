package com.raphaelfavero.stylearena.styleupload

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.raphaelfavero.stylearena.core.network.SessionManager
import com.raphaelfavero.stylearena.core.network.StyleArenaService
import com.raphaelfavero.stylearena.core.network.toPartMap
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

interface StyleUploadRepository {
    suspend fun uploadImage(bitmap: Bitmap): Boolean

    class Impl @Inject constructor(
        private val api: StyleArenaService,
        private val session: SessionManager,
        @ApplicationContext private val context: Context
    ) :
        StyleUploadRepository {

        override suspend fun uploadImage(bitmap: Bitmap): Boolean {
            return try {
                val url = api.getUploadUrl(session.sessionId)
                Log.v("URL", "S3 url : ${url.body()?.url} - ${url.body()?.fields}")
                val s3Url = url.body()!!.url
                val fields = url.body()!!.fields

                val file = bitmapToFile(bitmap)

                val response = api.uploadImage(
                    s3Url,
                    fields.toPartMap(),
                    image = MultipartBody.Part.createFormData(
                        "file",
                        fields.key,
                        file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                )
                response.isSuccessful

            } catch (e: IOException) {
                e.printStackTrace()
                false
            } catch (e: HttpException) {
                e.printStackTrace()
                false
            }
        }

        fun bitmapToFile(bitmap: Bitmap, fileName: String = "captured_image.jpg"): File {
            val file = File(context.cacheDir, fileName)
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapData = bos.toByteArray()

            FileOutputStream(file).use { fos ->
                fos.write(bitmapData)
                fos.flush()
            }
            return file
        }
    }
}
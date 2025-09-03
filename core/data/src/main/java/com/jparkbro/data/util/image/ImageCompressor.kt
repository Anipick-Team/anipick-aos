package com.jparkbro.data.util.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.roundToInt

class ImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun compressImage(
        contentUri: Uri,
        compressionThreshold: Long
    ): ByteArray? {
        return withContext(Dispatchers.IO) {
            val mimeType = context.contentResolver.getType(contentUri)
            val inputBytes = context
                .contentResolver
                .openInputStream(contentUri)
                ?.use { inputStream ->
                    inputStream.readBytes()
                } ?: return@withContext null

            ensureActive()

            withContext(Dispatchers.Default) {
                val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)

                ensureActive()

                // EXIF 정보를 읽어서 회전 각도 확인
                val rotatedBitmap = try {
                    val inputStream = context.contentResolver.openInputStream(contentUri)
                    val exif = ExifInterface(inputStream!!)
                    inputStream.close()

                    val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED
                    )

                    val rotation = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                        else -> 0f
                    }

                    if (rotation != 0f) {
                        val matrix = Matrix().apply { postRotate(rotation) }
                        Bitmap.createBitmap(
                            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                        )
                    } else {
                        bitmap
                    }
                } catch (e: Exception) {
                    bitmap // EXIF 읽기 실패 시 원본 사용
                }

                ensureActive()

                val compressFormat = when(mimeType) {
                    "image/png" -> Bitmap.CompressFormat.PNG
                    "image/jpeg" -> Bitmap.CompressFormat.JPEG
                    "image/webp" -> Bitmap.CompressFormat.WEBP_LOSSLESS
                    else -> Bitmap.CompressFormat.JPEG
                }

                var outputBytes: ByteArray
                var quality = 90
                do {
                    ByteArrayOutputStream().use { outputStream ->
                        rotatedBitmap.compress(compressFormat, quality, outputStream)
                        outputBytes = outputStream.toByteArray()
                        quality -= (quality * 0.1).roundToInt()
                    }
                } while(isActive &&
                    outputBytes.size > compressionThreshold &&
                    quality > 5 &&
                    compressFormat != Bitmap.CompressFormat.PNG
                )

                // 메모리 해제
                if (rotatedBitmap != bitmap) {
                    bitmap.recycle()
                }
                rotatedBitmap.recycle()

                outputBytes
            }
        }
    }
}
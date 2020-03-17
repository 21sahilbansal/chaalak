package com.loconav.locodriver.expense

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.IMAGE_QUALITY_CONSTANT
import com.loconav.locodriver.application.LocoDriverApplication
import com.loconav.locodriver.util.FileUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


object ImageUtil {

    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY_CONSTANT, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getMultipartFromUri(
        partName: String,
        listImageUri: List<String>?
    ): List<MultipartBody.Part> {
        val parts = ArrayList<MultipartBody.Part>()
        if (!listImageUri.isNullOrEmpty()) {
            for (item in listImageUri) {
                val uri = Uri.parse(item)
                val imageFile =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        FileUtil.getFile(LocoDriverApplication.instance.applicationContext, uri)
                            ?: break
                    } else {
                        File(item)
                    }
                val extension =
                    imageFile.absolutePath.substring(imageFile.absolutePath.lastIndexOf(".") + 1)

                val mediaType = MediaType.parse("image/${extension}")
                val requestImageFile = RequestBody.create(mediaType, imageFile)
                parts.add(
                    MultipartBody.Part.createFormData(
                        partName,
                        imageFile.name,
                        requestImageFile
                    )
                )
            }
        }
        return parts
    }
}
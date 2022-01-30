package com.naim.androd11supportedfilemanager.picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.naim.androd11supportedfilemanager.util.Android11SupportedFileUtil
import com.naim.androd11supportedfilemanager.util.SupportedFileAnnotationType
import com.naim.androd11supportedfilemanager.util.getSupportedMimeType
import java.io.File

object Android11SupportedFileManager : IAndroid11SupportedFilePicker {
    override fun openFilePickerIntent(
        fileList: List<SupportedFileAnnotationType.Type>?
    ): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            fileList?.let {
                this.putExtra(
                    Intent.EXTRA_MIME_TYPES,
                    getSupportedMimeType(fileList)
                )
            }
        }
    }
}

class FileManagerLifeCycleObserver constructor(
    private val context: Context,
    private val registry: ActivityResultRegistry,
    private val onSuccess: (String) -> Unit = {}
) :
    DefaultLifecycleObserver {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent?>

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        resultLauncher =
            registry.register(
                "key",
                owner,
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val file: File? = Android11SupportedFileUtil.from(context, result.data?.data!!)
                    println("File Path " + file)
                    onSuccess.invoke(file.toString())
                }

            }
    }

    fun getFilePickerIntent(
        fileList: List<SupportedFileAnnotationType.Type>?
    ) {
        resultLauncher.launch(Android11SupportedFileManager.openFilePickerIntent(fileList))
    }
}
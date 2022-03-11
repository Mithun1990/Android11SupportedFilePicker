package com.naim.androd11supportedfilemanager.picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.naim.androd11supportedfilemanager.model.SupportedFile
import com.naim.androd11supportedfilemanager.util.Android11SupportedFileUtil
import com.naim.androd11supportedfilemanager.util.SupportedFileAnnotationType
import com.naim.androd11supportedfilemanager.util.getSupportedMimeType
import java.io.File


object Android11SupportedFileManager : IAndroid11SupportedFilePicker {

    override fun openFilePickerIntent(
        fileList: List<SupportedFileAnnotationType.Type>?,
        isMultipleSelectionAllowed: Boolean
    ): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            fileList?.let {
                this.putExtra(
                    Intent.EXTRA_MIME_TYPES,
                    getSupportedMimeType(fileList)
                )
                this.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultipleSelectionAllowed)
            }
        }
    }
}

class FilePickerLifeCycleObserver constructor(
    private val context: Context,
    private val registry: ActivityResultRegistry,
    private val onFilePicked: (SupportedFile?, List<SupportedFile>?, Any?) -> Unit = { _, _, _ -> }
) :
    DefaultLifecycleObserver {
    private var passAdditionalObjectRequiredAfterFilePicked: Any? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent?>

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        resultLauncher =
            registry.register(
                "key",
                owner,
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                try {
                    if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                        val supportedFileList = mutableListOf<SupportedFile>()
                        result.data?.data?.let { data ->
                            val file: File? = Android11SupportedFileUtil.from(context, data)
                            val fileName: String? =
                                file?.let { Android11SupportedFileUtil.getFileName(it.absolutePath) }
                            val fileExt: String? =
                                fileName?.let { Android11SupportedFileUtil.getFileExt(it) }
                            onFilePicked.invoke(
                                SupportedFile(
                                    fileName,
                                    file,
                                    fileExt
                                ), null,
                                passAdditionalObjectRequiredAfterFilePicked
                            )
                            passAdditionalObjectRequiredAfterFilePicked = null
                        }
                        result.data?.clipData?.let { data ->
                            when {
                                data.itemCount == 1 -> {
                                    val file: File? =
                                        Android11SupportedFileUtil.from(
                                            context,
                                            data.getItemAt(0).uri
                                        )
                                    val fileName: String? =
                                        file?.let { Android11SupportedFileUtil.getFileName(it.absolutePath) }
                                    val fileExt: String? =
                                        fileName?.let { Android11SupportedFileUtil.getFileExt(it) }
                                    onFilePicked.invoke(
                                        SupportedFile(
                                            fileName,
                                            file,
                                            fileExt
                                        ), null,
                                        passAdditionalObjectRequiredAfterFilePicked
                                    )
                                    passAdditionalObjectRequiredAfterFilePicked = null
                                }
                                data.itemCount > 1 -> {
                                    for (i in 0 until data.itemCount) {
                                        val file: File? =
                                            Android11SupportedFileUtil.from(
                                                context,
                                                data.getItemAt(i).uri
                                            )
                                        val fileName: String? =
                                            file?.let { Android11SupportedFileUtil.getFileName(it.absolutePath) }
                                        val fileExt: String? =
                                            fileName?.let { Android11SupportedFileUtil.getFileExt(it) }
                                        supportedFileList.add(
                                            SupportedFile(
                                                fileName,
                                                file,
                                                fileExt
                                            )
                                        )
                                    }
                                    onFilePicked.invoke(
                                        null,
                                        supportedFileList,
                                        passAdditionalObjectRequiredAfterFilePicked
                                    )
                                    passAdditionalObjectRequiredAfterFilePicked = null
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    onFilePicked.invoke(null, null, null)
                }
            }
    }

    fun getFilePickerIntent(
        fileList: List<SupportedFileAnnotationType.Type>?,
        isMultipleSelectionAllowed: Boolean = false,
        passAdditionalObjectRequiredAfterFilePicked: Any? = null
    ) {
        this.passAdditionalObjectRequiredAfterFilePicked =
            passAdditionalObjectRequiredAfterFilePicked
        resultLauncher.launch(
            Android11SupportedFileManager.openFilePickerIntent(fileList, isMultipleSelectionAllowed)
        )
    }
}

@Deprecated("This is class is deprecated", replaceWith = ReplaceWith("FilePickerLifeCycleObserver"))
class FileManagerLifeCycleObserver constructor(
    private val context: Context,
    private val registry: ActivityResultRegistry,
    private val onSuccess: (SupportedFile) -> Unit = {}
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
                    val fileName: String? =
                        file?.let { Android11SupportedFileUtil.getFileName(it.absolutePath) }
                    val fileExt: String? =
                        fileName?.let { Android11SupportedFileUtil.getFileExt(it) }
                    onSuccess.invoke(SupportedFile(fileName, file, fileExt))
                }

            }
    }
}
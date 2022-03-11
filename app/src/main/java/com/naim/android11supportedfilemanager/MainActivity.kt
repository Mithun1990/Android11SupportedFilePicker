package com.naim.android11supportedfilemanager

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.naim.androd11supportedfilemanager.model.SupportedFile
import com.naim.androd11supportedfilemanager.picker.FilePickerLifeCycleObserver
import com.naim.androd11supportedfilemanager.util.SupportedFileAnnotationType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val observer =
            FilePickerLifeCycleObserver(
                this@MainActivity,
                this.activityResultRegistry,
                supportedFileResult
            )
        lifecycle.addObserver(observer)

        findViewById<Button>(R.id.button).setOnClickListener {
            observer.getFilePickerIntent(
                mutableListOf(
                    SupportedFileAnnotationType.Type.ALL
                ),
                true,
                null
            )
        }
    }

    private val supportedFileResult: (SupportedFile?, List<SupportedFile>?, Any?) -> Unit =
        { supportedFile, supportedFileList, passAdditionalObjectRequiredAfterFilePicked ->
            /* Single File Selection result*/
            supportedFile?.let {
                println("Relative file path   ${it.file}  ${it.fileName}")
                passAdditionalObjectRequiredAfterFilePicked?.let {
                    val additionalFileInfo: AdditionalFileInfo =
                        (passAdditionalObjectRequiredAfterFilePicked as AdditionalFileInfo)
                    println("Additional file info ${additionalFileInfo.fileId} ${additionalFileInfo.type}")
                }
            }
            /* Multiple File Selection result*/
            supportedFileList?.let {
                for (i in it) {
                    println("Relative file path   ${i.file}  ${i.fileName}")
                }
                passAdditionalObjectRequiredAfterFilePicked?.let {
                    val additionalFileInfo: AdditionalFileInfo =
                        (passAdditionalObjectRequiredAfterFilePicked as AdditionalFileInfo)
                    println("Additional file info ${additionalFileInfo.fileId} ${additionalFileInfo.type}")
                }
            }
        }
}
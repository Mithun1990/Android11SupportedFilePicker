package com.naim.android11supportedfilemanager

import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.naim.androd11supportedfilemanager.picker.FileManagerLifeCycleObserver
import com.naim.androd11supportedfilemanager.util.SupportedFileAnnotationType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val observer =
            FileManagerLifeCycleObserver(
                this@MainActivity,
                this.activityResultRegistry,
                filePath
            )
        lifecycle.addObserver(observer)

        findViewById<Button>(R.id.button).setOnClickListener {
            observer.getFilePickerIntent(
                mutableListOf(
                    SupportedFileAnnotationType.Type.ALL
                )
            )
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {

            }
        }

    private val filePath: (String) -> Unit =
        { filePath -> println("Relative file path " + filePath) }
}
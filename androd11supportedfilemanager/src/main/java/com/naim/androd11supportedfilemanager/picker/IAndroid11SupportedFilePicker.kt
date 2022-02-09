package com.naim.androd11supportedfilemanager.picker

import android.content.Intent
import com.naim.androd11supportedfilemanager.util.SupportedFileAnnotationType

interface IAndroid11SupportedFilePicker {
    fun openFilePickerIntent(
        fileList: List<SupportedFileAnnotationType.Type>?,
        isMultipleSelectionAllowed: Boolean
    ): Intent
}
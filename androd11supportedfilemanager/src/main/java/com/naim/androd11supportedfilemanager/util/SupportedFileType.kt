package com.naim.androd11supportedfilemanager.util

val supportedFileList = mutableMapOf(
    SupportedFileAnnotationType.Type.IMAGE to "image/*",
    SupportedFileAnnotationType.Type.PDF to "application/pdf",
    SupportedFileAnnotationType.Type.MS_DOC to "application/doc",
    SupportedFileAnnotationType.Type.MS_DOCX to "application/docx",
    SupportedFileAnnotationType.Type.ALL to "*/*"
)

fun getSupportedMimeType(fileList: List<SupportedFileAnnotationType.Type>): Array<String?> {
    return if (fileList.isNotEmpty()) {
        val typeList = arrayListOf<String?>()
        for (fileType in fileList) {
            typeList.add(supportedFileList[fileType])
        }
        return typeList.toTypedArray()
    } else {
        arrayOf(supportedFileList[SupportedFileAnnotationType.Type.ALL])
    }
}
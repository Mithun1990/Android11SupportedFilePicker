package com.naim.androd11supportedfilemanager.util

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY
)

annotation class SupportedFileAnnotationType {
    enum class Type {
        IMAGE,
        PDF,
        MS_DOC,
        MS_DOCX,
        ALL
    }
}

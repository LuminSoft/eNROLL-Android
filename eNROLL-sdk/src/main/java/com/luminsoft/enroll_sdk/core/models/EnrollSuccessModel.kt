package com.luminsoft.enroll_sdk.core.models

data class EnrollSuccessModel(
    val enrollMessage: String,
    val documentId: String? = null,
    val applicantId: String? = null,
    // New fields for exit step functionality - clients can use requestId to resume later
    val requestId: String? = null,
    val exitStepCompleted: Boolean = false,
    val completedStepName: String? = null
)

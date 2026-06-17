package com.luminsoft.enroll_sdk.core.models

enum class EnrollContractSignatureMode(val value: Int) {
    LOW_RISK(1),
    HIGH_RISK(2),
    LOW_RISK_FRA(5),
    HIGH_RISK_FRA(4)
}

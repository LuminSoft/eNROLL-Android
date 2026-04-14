package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload

/**
 * Error codes for NFC ePassport reading failures, sent to the backend
 * via the FailingPassport API endpoint.
 */
enum class NfcErrorCode(val code: String) {
    NFCUserCanceledScan("10208"),
    NFCGeneralError("10209"),
    NFCConnectionError("10210"),
    NFCTimeOutError("10211"),
    NFCInvalidMRZKey("10212"),
}

package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload

import android.graphics.Bitmap
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper

/**
 * Maps Innovatrics [NfcTravelDocumentReaderResult] to [PassportNfcUploadRequest]
 * for uploading passport NFC data to the backend.
 */
object NfcResultMapper {

    /**
     * Converts NFC travel document result and passport image to the API request model.
     *
     * @param passportImage The passport page image captured by the camera (MRZ scan step)
     * @param faceBitmap The face image extracted from the NFC chip
     * @param nfcResult The full NFC travel document reader result from Innovatrics
     * @return [PassportNfcUploadRequest] ready to be sent to the backend
     */
    fun toUploadRequest(
        passportImage: Bitmap,
        faceBitmap: Bitmap?,
        nfcResult: NfcTravelDocumentReaderResult,
    ): PassportNfcUploadRequest {
        val travelDoc = nfcResult.travelDocument
        val td3 = travelDoc.machineReadableZoneInformation?.machineReadableZone?.td3

        val passportImageBase64 = DotHelper.bitmapToBase64(passportImage)
        val faceImageBase64 = faceBitmap?.let { DotHelper.bitmapToBase64(it) }

        return PassportNfcUploadRequest(
            image = passportImageBase64.takeIf { it.isNotBlank() },
            faceImage = faceImageBase64?.takeIf { it.isNotBlank() },
            travelDocument = TravelDocumentRequest(
                additionalPersonalDetails = travelDoc.additionalPersonalDetails?.let { details ->
                    AdditionalPersonalDetailsRequest(
                        otherNames = details.otherNames ?: emptyList(),
                        address = details.address,
                        nameOfHolder = details.nameOfHolder?.let { name ->
                            NameOfHolderRequest(
                                secondaryIdentifier = name.secondaryIdentifier,
                                primaryIdentifier = name.primaryIdentifier,
                            )
                        },
                        fullDateOfBirth = details.fullDateOfBirth,
                    )
                },
                additionalDocumentDetails = travelDoc.additionalDocumentDetails?.let { details ->
                    AdditionalDocumentDetailsRequest(
                        issuingAuthority = details.issuingAuthority,
                        dateOfIssue = details.dateOfIssue,
                    )
                },
                ldsVersion = travelDoc.ldsVersion,
                accessControlProtocolUsed = travelDoc.accessControlProtocolUsed?.name,
                machineReadableZoneInformation = travelDoc.machineReadableZoneInformation?.let { mrzInfo ->
                    val mrz = mrzInfo.machineReadableZone
                    MachineReadableZoneInformationRequest(
                        machineReadableZone = mrz?.let {
                            MachineReadableZoneRequest(
                                td3 = td3?.let { t ->
                                    Td3Request(
                                        name = Td3NameRequest(
                                            secondaryIdentifier = t.name?.secondaryIdentifier?.let { f ->
                                                MrzFieldRequest(
                                                    value = f.value,
                                                    positions = f.positions?.map { pos ->
                                                        MrzPositionRequest(
                                                            startIndex = pos.startIndex,
                                                            endIndex = pos.endIndex,
                                                            lineIndex = pos.lineIndex
                                                        )
                                                    }
                                                )
                                            },
                                            primaryIdentifier = t.name?.primaryIdentifier?.let { f ->
                                                MrzFieldRequest(
                                                    value = f.value,
                                                    positions = f.positions?.map { pos ->
                                                        MrzPositionRequest(
                                                            startIndex = pos.startIndex,
                                                            endIndex = pos.endIndex,
                                                            lineIndex = pos.lineIndex
                                                        )
                                                    }
                                                )
                                            },
                                        ),
                                        dateOfBirth = t.dateOfBirth?.let { f -> 
                                            MrzFieldRequest(
                                                value = f.value,
                                                positions = f.positions?.map { pos ->
                                                    MrzPositionRequest(
                                                        startIndex = pos.startIndex,
                                                        endIndex = pos.endIndex,
                                                        lineIndex = pos.lineIndex
                                                    )
                                                }
                                            )
                                        },
                                        documentCode = t.documentCode?.let { f -> 
                                            MrzFieldRequest(
                                                value = f.value,
                                                positions = f.positions?.map { pos ->
                                                    MrzPositionRequest(
                                                        startIndex = pos.startIndex,
                                                        endIndex = pos.endIndex,
                                                        lineIndex = pos.lineIndex
                                                    )
                                                }
                                            )
                                        },
                                        sex = t.sex?.let { f -> 
                                            MrzFieldRequest(
                                                value = f.value,
                                                positions = f.positions?.map { pos ->
                                                    MrzPositionRequest(
                                                        startIndex = pos.startIndex,
                                                        endIndex = pos.endIndex,
                                                        lineIndex = pos.lineIndex
                                                    )
                                                }
                                            )
                                        },
                                        personalNumberOrOtherOptionalData = t.personalNumberOrOtherOptionalData?.let { f ->
                                            MrzFieldRequest(
                                                value = f.value,
                                                positions = f.positions?.map { pos ->
                                                    MrzPositionRequest(
                                                        startIndex = pos.startIndex,
                                                        endIndex = pos.endIndex,
                                                        lineIndex = pos.lineIndex
                                                    )
                                                }
                                            )
                                        },
                                        dateOfExpiry = t.dateOfExpiry?.let { f -> 
                                            MrzFieldRequest(
                                                value = f.value,
                                                positions = f.positions?.map { pos ->
                                                    MrzPositionRequest(
                                                        startIndex = pos.startIndex,
                                                        endIndex = pos.endIndex,
                                                        lineIndex = pos.lineIndex
                                                    )
                                                }
                                            )
                                        },
                                        passportNumber = t.passportNumber?.let { f -> 
                                            MrzFieldRequest(
                                                value = f.value,
                                                positions = f.positions?.map { pos ->
                                                    MrzPositionRequest(
                                                        startIndex = pos.startIndex,
                                                        endIndex = pos.endIndex,
                                                        lineIndex = pos.lineIndex
                                                    )
                                                }
                                            )
                                        },
                                        nationality = t.nationality?.let { f -> 
                                            MrzFieldRequest(
                                                value = f.value,
                                                positions = f.positions?.map { pos ->
                                                    MrzPositionRequest(
                                                        startIndex = pos.startIndex,
                                                        endIndex = pos.endIndex,
                                                        lineIndex = pos.lineIndex
                                                    )
                                                }
                                            )
                                        },
                                        compositeCheckDigit = t.compositeCheckDigit?.let { f ->
                                            MrzFieldRequest(
                                                value = f.value?.toString(),
                                                isValid = f.isValid
                                            )
                                        },
                                        issuingStateOrOrganization = t.issuingStateOrOrganization?.let { f ->
                                            MrzFieldRequest(
                                                value = f.value,
                                                positions = f.positions?.map { pos ->
                                                    MrzPositionRequest(
                                                        startIndex = pos.startIndex,
                                                        endIndex = pos.endIndex,
                                                        lineIndex = pos.lineIndex
                                                    )
                                                }
                                            )
                                        },
                                    )
                                },
                                lines = it.lines,
                            )
                        },
                    )
                },
                authenticationStatus = travelDoc.authenticationStatus?.let { authStatus ->
                    AuthenticationStatusRequest(
                        chip = authStatus.chip?.let { chip ->
                            AuthenticationDetailRequest(
                                authenticationProtocol = "chipAuthentication",
                                status = chip.status?.name?.lowercase()
                            )
                        },
                        data = authStatus.data?.let { data ->
                            AuthenticationDetailRequest(
                                authenticationProtocol = "passiveAuthentication",
                                status = data.status?.name?.lowercase()
                            )
                        }
                    )
                },
            ),
        )
    }
}

package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload

import android.graphics.Bitmap
import android.util.Base64
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import java.security.MessageDigest

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
                algorithmHash = "SHA256",
                ldsMasterFile = travelDoc.ldsMasterFile?.lds1eMrtdApplication?.let { app ->
                    LdsMasterFileRequest(
                        lds1eMrtdApplication = Lds1eMrtdApplicationRequest(
                            dg1MachineReadableZoneInformation = bytesToBase64(app.dg1MachineReadableZoneInformation?.bytes),
                            dg2EncodedIdentificationFeaturesFace = bytesToBase64(app.dg2EncodedIdentificationFeaturesFace?.bytes),
                            dg3AdditionalIdentificationFeatureFingers = bytesToBase64(app.dg3AdditionalIdentificationFeatureFingers?.bytes),
                            dg4AdditionalIdentificationFeatureIrises = bytesToBase64(app.dg4AdditionalIdentificationFeatureIrises?.bytes),
                            dg5DisplayedPortrait = bytesToBase64(app.dg5DisplayedPortrait?.bytes),
                            dg7DisplayedSignatureOrUsualMark = bytesToBase64(app.dg7DisplayedSignatureOrUsualMark?.bytes),
                            dg8DataFeatures = bytesToBase64(app.dg8DataFeatures?.bytes),
                            dg9StructureFeatures = bytesToBase64(app.dg9StructureFeatures?.bytes),
                            dg10SubstanceFeature = bytesToBase64(app.dg10SubstanceFeatures?.bytes),
                            dg11AdditionalPersonalDetails = bytesToBase64(app.dg11AdditionalPersonalDetails?.bytes),
                            dg12AdditionalDocumentDetails = bytesToBase64(app.dg12AdditionalDocumentDetails?.bytes),
                            dg13OptionalDetail = bytesToBase64(app.dg13OptionalDetails?.bytes),
                            dg14SecurityOptions = bytesToBase64(app.dg14SecurityOptions?.bytes),
                            dg15ActiveAuthenticationPublicKeyInfo = bytesToBase64(app.dg15ActiveAuthenticationPublicKeyInfo?.bytes),
                            dg16PersonsToNotif = bytesToBase64(app.dg16PersonsToNotify?.bytes),
                            sodDocumentSecurityObject = bytesToBase64(app.sodDocumentSecurityObject?.bytes),
                        )
                    )
                },
            ),
        )
    }

    /**
     * Hashes a byte array with SHA-256 and returns the digest as a Base64-encoded string.
     * Returns null if the input is null or empty.
     */
    private fun bytesToBase64(bytes: ByteArray?): String? {
        if (bytes == null || bytes.isEmpty()) return null
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return Base64.encodeToString(digest, Base64.NO_WRAP)
    }
}

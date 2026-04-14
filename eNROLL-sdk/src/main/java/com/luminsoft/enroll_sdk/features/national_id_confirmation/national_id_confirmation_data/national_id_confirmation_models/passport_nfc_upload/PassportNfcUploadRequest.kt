package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload

import com.google.gson.annotations.SerializedName

data class PassportNfcUploadRequest(
    @SerializedName("image")
    val image: String? = null,

    @SerializedName("faceImage")
    val faceImage: String? = null,

    @SerializedName("travelDocument")
    val travelDocument: TravelDocumentRequest? = null,
)

data class TravelDocumentRequest(
    @SerializedName("additionalPersonalDetails")
    val additionalPersonalDetails: AdditionalPersonalDetailsRequest? = null,

    @SerializedName("additionalDocumentDetails")
    val additionalDocumentDetails: AdditionalDocumentDetailsRequest? = null,

    @SerializedName("ldsVersion")
    val ldsVersion: String? = null,

    @SerializedName("accessControlProtocolUsed")
    val accessControlProtocolUsed: String? = null,

    @SerializedName("machineReadableZoneInformation")
    val machineReadableZoneInformation: MachineReadableZoneInformationRequest? = null,

    @SerializedName("authenticationStatus")
    val authenticationStatus: AuthenticationStatusRequest? = null,

    @SerializedName("ldsMasterFile")
    val ldsMasterFile: LdsMasterFileRequest? = null,

    @SerializedName("algorithmHash")
    val algorithmHash: String? = null,
)

data class AdditionalPersonalDetailsRequest(
    @SerializedName("otherNames")
    val otherNames: List<String>? = null,

    @SerializedName("address")
    val address: List<String>? = null,

    @SerializedName("nameOfHolder")
    val nameOfHolder: NameOfHolderRequest? = null,

    @SerializedName("fullDateOfBirth")
    val fullDateOfBirth: String? = null,
)

data class NameOfHolderRequest(
    @SerializedName("secondaryIdentifier")
    val secondaryIdentifier: String? = null,

    @SerializedName("primaryIdentifier")
    val primaryIdentifier: String? = null,
)

data class AdditionalDocumentDetailsRequest(
    @SerializedName("issuingAuthority")
    val issuingAuthority: String? = null,

    @SerializedName("dateOfIssue")
    val dateOfIssue: String? = null,
)

data class MachineReadableZoneInformationRequest(
    @SerializedName("machineReadableZone")
    val machineReadableZone: MachineReadableZoneRequest? = null,
)

data class MachineReadableZoneRequest(
    @SerializedName("td3")
    val td3: Td3Request? = null,

    @SerializedName("lines")
    val lines: List<String>? = null,
)

data class Td3Request(
    @SerializedName("name")
    val name: Td3NameRequest? = null,

    @SerializedName("dateOfBirth")
    val dateOfBirth: MrzFieldRequest? = null,

    @SerializedName("documentCode")
    val documentCode: MrzFieldRequest? = null,

    @SerializedName("sex")
    val sex: MrzFieldRequest? = null,

    @SerializedName("personalNumberOrOtherOptionalData")
    val personalNumberOrOtherOptionalData: MrzFieldRequest? = null,

    @SerializedName("dateOfExpiry")
    val dateOfExpiry: MrzFieldRequest? = null,

    @SerializedName("passportNumber")
    val passportNumber: MrzFieldRequest? = null,

    @SerializedName("nationality")
    val nationality: MrzFieldRequest? = null,

    @SerializedName("compositeCheckDigit")
    val compositeCheckDigit: MrzFieldRequest? = null,

    @SerializedName("issuingStateOrOrganization")
    val issuingStateOrOrganization: MrzFieldRequest? = null,
)

data class Td3NameRequest(
    @SerializedName("secondaryIdentifier")
    val secondaryIdentifier: MrzFieldRequest? = null,

    @SerializedName("primaryIdentifier")
    val primaryIdentifier: MrzFieldRequest? = null,
)

data class MrzFieldRequest(
    @SerializedName("value")
    val value: String? = null,

    @SerializedName("positions")
    val positions: List<MrzPositionRequest>? = null,

    @SerializedName("isValid")
    val isValid: Boolean? = null,
)

data class MrzPositionRequest(
    @SerializedName("startIndex")
    val startIndex: Int? = null,

    @SerializedName("endIndex")
    val endIndex: Int? = null,

    @SerializedName("lineIndex")
    val lineIndex: Int? = null,
)

data class AuthenticationStatusRequest(
    @SerializedName("chip")
    val chip: AuthenticationDetailRequest? = null,

    @SerializedName("data")
    val data: AuthenticationDetailRequest? = null,
)

data class LdsMasterFileRequest(
    @SerializedName("lds1eMrtdApplication")
    val lds1eMrtdApplication: Lds1eMrtdApplicationRequest? = null,
)

data class Lds1eMrtdApplicationRequest(
    @SerializedName("dg1MachineReadableZoneInformation")
    val dg1MachineReadableZoneInformation: String? = null,

    @SerializedName("dg2EncodedIdentificationFeaturesFace")
    val dg2EncodedIdentificationFeaturesFace: String? = null,

    @SerializedName("dg3AdditionalIdentificationFeatureFingers")
    val dg3AdditionalIdentificationFeatureFingers: String? = null,

    @SerializedName("dg4AdditionalIdentificationFeatureIrises")
    val dg4AdditionalIdentificationFeatureIrises: String? = null,

    @SerializedName("dg5DisplayedPortrait")
    val dg5DisplayedPortrait: String? = null,

    @SerializedName("dg7DisplayedSignatureOrUsualMark")
    val dg7DisplayedSignatureOrUsualMark: String? = null,

    @SerializedName("dg8DataFeatures")
    val dg8DataFeatures: String? = null,

    @SerializedName("dg9StructureFeatures")
    val dg9StructureFeatures: String? = null,

    @SerializedName("dg10SubstanceFeature")
    val dg10SubstanceFeature: String? = null,

    @SerializedName("dg11AdditionalPersonalDetails")
    val dg11AdditionalPersonalDetails: String? = null,

    @SerializedName("dg12AdditionalDocumentDetails")
    val dg12AdditionalDocumentDetails: String? = null,

    @SerializedName("dg13OptionalDetail")
    val dg13OptionalDetail: String? = null,

    @SerializedName("dg14SecurityOptions")
    val dg14SecurityOptions: String? = null,

    @SerializedName("dg15ActiveAuthenticationPublicKeyInfo")
    val dg15ActiveAuthenticationPublicKeyInfo: String? = null,

    @SerializedName("dg16PersonsToNotif")
    val dg16PersonsToNotif: String? = null,

    @SerializedName("sodDocumentSecurityObject")
    val sodDocumentSecurityObject: String? = null,
)

data class AuthenticationDetailRequest(
    @SerializedName("authenticationProtocol")
    val authenticationProtocol: String? = null,

    @SerializedName("status")
    val status: String? = null,
)

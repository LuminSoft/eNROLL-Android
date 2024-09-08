
import com.google.gson.annotations.SerializedName

data class UpdatePassportConfirmationResponse(

    @SerializedName("isSuccess") var isSuccess: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("passportData") var passportData: UpdatePassportCustomerData? = UpdatePassportCustomerData(),
    @SerializedName("customerData") var customerData: UpdatePassportCustomerData? = UpdatePassportCustomerData(),
)

data class UpdatePassportCustomerData(
    @SerializedName("idFrontScan") var idFrontScan: String? = null,
    @SerializedName("idBackScan") var idBackScan: String? = null,
    @SerializedName("photo") var photo: String? = null,
    @SerializedName("fullName") var fullName: String? = null,
    @SerializedName("fullNameEn") var fullNameEn: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("firstNameEn") var firstNameEn: String? = null,
    @SerializedName("familyName") var familyName: String? = null,
    @SerializedName("familyNameEn") var familyNameEn: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("birthdate") var birthdate: String? = null,
    @SerializedName("idNumber") var idNumber: String? = null,
    @SerializedName("idNumberBack") var idNumberBack: String? = null,
    @SerializedName("documentNumber") var documentNumber: String? = null,
    @SerializedName("documentTypeId") var documentTypeId: String? = null,
    @SerializedName("documentTypeCode") var documentTypeCode: String? = null,
    @SerializedName("issueDate") var issueDate: String? = null,
    @SerializedName("profession") var profession: String? = null,
    @SerializedName("maritalStatus") var maritalStatus: String? = null,
    @SerializedName("religion") var religion: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("nationality") var nationality: String? = null,
    @SerializedName("detectedAge") var detectedAge: String? = null,
    @SerializedName("detectedGender") var detectedGender: String? = null,
    @SerializedName("fullNameAr") var fullNameAr: String? = null,
    @SerializedName("firstNameAr") var firstNameAr: String? = null,
    @SerializedName("familyNameAr") var familyNameAr: String? = null,
    @SerializedName("documentCode") var documentCode: String? = null,
    @SerializedName("issuingAuthority") var issuingAuthority: String? = null,
    @SerializedName("visualZone") var visualZone: String? = null,
    @SerializedName("expirationDate") var expirationDate: String? = null
)
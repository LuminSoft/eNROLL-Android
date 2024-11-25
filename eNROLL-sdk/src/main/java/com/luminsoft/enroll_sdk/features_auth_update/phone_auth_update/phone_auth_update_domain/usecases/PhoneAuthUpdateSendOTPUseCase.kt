
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpRequestModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpResponseModel

class PhoneAuthUpdateSendOTPUseCase(private val phoneRepository: PhoneAuthUpdateRepository) :
    UseCase<Either<SdkFailure, SendPhoneOtpResponseModel>, SendPhoneOtpRequestParams> {

    override suspend fun call(params: SendPhoneOtpRequestParams): Either<SdkFailure, SendPhoneOtpResponseModel> {
        val validateOTPRequestModel = SendPhoneOtpRequestModel()
        validateOTPRequestModel.updateStep = params.updateStep
        return phoneRepository.sendPhoneAuthUpdateOtp(validateOTPRequestModel)
    }
}

data class SendPhoneOtpRequestParams(
    val updateStep: Int? = null
)
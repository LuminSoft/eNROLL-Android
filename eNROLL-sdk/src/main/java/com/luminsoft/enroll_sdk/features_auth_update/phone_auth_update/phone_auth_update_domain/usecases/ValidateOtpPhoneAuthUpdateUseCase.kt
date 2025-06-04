
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.ValidatePhoneOtpRequestModel

class ValidateOtpPhoneAuthUpdateUseCase(private val phoneRepository: PhoneAuthUpdateRepository) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpPhoneAuthUpdateUseCaseParams> {

    override suspend fun call(params: ValidateOtpPhoneAuthUpdateUseCaseParams): Either<SdkFailure, Null> {
        val validateOTPRequestModel = ValidatePhoneOtpRequestModel()
        validateOTPRequestModel.otp = params.otp
        validateOTPRequestModel.updateStep = params.updateStep
        return phoneRepository.validateOTPPhoneAuthUpdate(validateOTPRequestModel)
    }
}

data class ValidateOtpPhoneAuthUpdateUseCaseParams(
    val otp: String? = null,
    val updateStep: Int? = null
)

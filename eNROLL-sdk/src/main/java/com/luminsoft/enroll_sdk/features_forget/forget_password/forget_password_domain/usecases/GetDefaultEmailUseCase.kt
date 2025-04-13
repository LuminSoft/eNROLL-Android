
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.GetDefaultEmailResponseModel

class GetDefaultEmailUseCase(private val forgetPasswordRepository: ForgetPasswordRepository) :
    UseCase<Either<SdkFailure, GetDefaultEmailResponseModel>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, GetDefaultEmailResponseModel> {
        return forgetPasswordRepository.getDefaultEmail()
    }
}




import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.ForgetPasswordRequestModel
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.GetDefaultEmailResponseModel

class ForgetPasswordRepositoryImplementation(private val forgetPasswordRemoteDataSource: ForgetPasswordRemoteDataSource) :
    ForgetPasswordRepository {

    override suspend fun getDefaultEmail(): Either<SdkFailure, GetDefaultEmailResponseModel> {
        return when (val response = forgetPasswordRemoteDataSource.getDefaultEmail()) {
            is BaseResponse.Success -> {
                Either.Right(response.data as GetDefaultEmailResponseModel)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun sendMailOtp(): Either<SdkFailure, Null> {

        return when (val response = forgetPasswordRemoteDataSource.sendMailOtp()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }
            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun validateOTPMail(request: ValidateOTPRequestModel): Either<SdkFailure, Null> {

        return when (val response = forgetPasswordRemoteDataSource.validateOTPMail(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }
            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun updatePassword(request: ForgetPasswordRequestModel): Either<SdkFailure, Null> {

        return when (val response = forgetPasswordRemoteDataSource.updatePassword(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }
            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

}


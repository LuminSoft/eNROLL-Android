

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth_update.password_auth_update.password_auth_update_data.password_auth_update_models.SetPasswordAuthUpdateRequest

class PasswordAuthUpdateRepositoryImplementation(private val passwordRemoteDataSource: PasswordAuthUpdateRemoteDataSource) :
    PasswordAuthUpdateRepository {
    override suspend fun verifyPassword(request: SetPasswordAuthUpdateRequest): Either<SdkFailure, Null> {
        return when (val response = passwordRemoteDataSource.verifyPassword(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}


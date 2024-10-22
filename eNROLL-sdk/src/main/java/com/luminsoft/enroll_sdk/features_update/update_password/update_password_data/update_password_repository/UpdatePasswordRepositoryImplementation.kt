

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class UpdatePasswordRepositoryImplementation(private val passwordRemoteDataSource: UpdatePasswordRemoteDataSource) :
    UpdatePasswordRepository {
    override suspend fun updatePassword(request: UpdatePasswordRequest): Either<SdkFailure, Null> {
        return when (val response = passwordRemoteDataSource.updatePassword(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}


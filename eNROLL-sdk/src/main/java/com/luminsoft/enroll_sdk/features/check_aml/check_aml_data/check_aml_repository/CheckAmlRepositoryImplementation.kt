

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class CheckAmlRepositoryImplementation(private val checkAmlRemoteDataSource: CheckAmlRemoteDataSource) :
    CheckAmlRepository {
    override suspend fun checkAml(): Either<SdkFailure, Null> {
        return when (val response = checkAmlRemoteDataSource.checkAml()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}


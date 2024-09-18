

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class ForgetLocationRepositoryImplementation(private val locationRemoteDataSource: ForgetLocationRemoteDataSource) :
    ForgetLocationRepository {
    override suspend fun forgetLocation(request: ForgetLocationRequestModel): Either<SdkFailure, Null> {
        return when (val response = locationRemoteDataSource.forgetLocation(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}


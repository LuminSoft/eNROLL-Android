

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class UpdateLocationRepositoryImplementation(private val locationRemoteDataSource: UpdateLocationRemoteDataSource) :
    UpdateLocationRepository {
    override suspend fun updateLocation(request: UpdateLocationRequestModel): Either<SdkFailure, Null> {
        return when (val response = locationRemoteDataSource.updateLocation(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}


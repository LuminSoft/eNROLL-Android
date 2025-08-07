
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class UpdateDeviceIdRepositoryImplementation(private val updateDeviceIdRemoteDataSource: UpdateDeviceIdRemoteDataSource) :
    UpdateDeviceIdRepository {
    override suspend fun updateDeviceId(request: UpdateDeviceIdRequestModel): Either<SdkFailure, Null> {
        return when (val response = updateDeviceIdRemoteDataSource.updateDeviceId(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }


}




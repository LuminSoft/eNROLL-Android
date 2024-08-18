
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class DeviceIdAuthUpdateRepositoryImplementation(private val deviceIdAuthUpdateRemoteDataSource: DeviceIdAuthUpdateRemoteDataSource) :
    DeviceIdAuthUpdateRepository {
    override suspend fun deviceIdAuthUpdate(request: CheckDeviceIdAuthUpdateRequestModel): Either<SdkFailure, Null> {
        return when (val response = deviceIdAuthUpdateRemoteDataSource.checkDeviceIdAuthUpdate(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }


}




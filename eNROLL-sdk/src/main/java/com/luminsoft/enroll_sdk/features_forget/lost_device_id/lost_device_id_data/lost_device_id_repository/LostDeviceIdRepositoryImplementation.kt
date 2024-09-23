
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.models.DeviceIdRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class LostDeviceIdRepositoryImplementation(private val lostDeviceIdRemoteDataSource: LostDeviceIdRemoteDataSource) :
    LostDeviceIdRepository {
    override suspend fun lostDeviceId(request: DeviceIdRequestModel): Either<SdkFailure, Null> {
        return when (val response = lostDeviceIdRemoteDataSource.lostDeviceId(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }


}




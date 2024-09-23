
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.models.DeviceIdRequestModel

interface LostDeviceIdRepository {
    suspend fun lostDeviceId(request: DeviceIdRequestModel): Either<SdkFailure, Null>
}
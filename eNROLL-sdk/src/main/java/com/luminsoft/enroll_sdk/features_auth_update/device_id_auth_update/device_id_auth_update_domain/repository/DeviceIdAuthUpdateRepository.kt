
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface DeviceIdAuthUpdateRepository {
    suspend fun deviceIdAuthUpdate(request: CheckDeviceIdAuthUpdateRequestModel): Either<SdkFailure, Null>
}
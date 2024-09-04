
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface UpdateDeviceIdRepository {
    suspend fun updateDeviceId(request: UpdateDeviceIdRequestModel): Either<SdkFailure, Null>
}
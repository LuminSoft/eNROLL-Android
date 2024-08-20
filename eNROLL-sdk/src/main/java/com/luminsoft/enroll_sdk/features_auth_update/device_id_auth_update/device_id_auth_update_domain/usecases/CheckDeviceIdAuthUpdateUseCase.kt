
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class CheckDeviceIdAuthUpdateUseCase(private val deviceIdAuthUpdateRepository: DeviceIdAuthUpdateRepository) :
    UseCase<Either<SdkFailure, Null>, CheckDeviceIdAuthUpdateUseCaseParams> {
    override suspend fun call(params: CheckDeviceIdAuthUpdateUseCaseParams): Either<SdkFailure, Null> {
        val checkDeviceIdAuthUpdate= CheckDeviceIdAuthUpdateRequestModel()
        checkDeviceIdAuthUpdate.imei = params.imei
        checkDeviceIdAuthUpdate.isFromWeb = params.isFromWeb
        checkDeviceIdAuthUpdate.updateStepId = params.updateStepId
        return deviceIdAuthUpdateRepository.deviceIdAuthUpdate(checkDeviceIdAuthUpdate)
    }

}

data class CheckDeviceIdAuthUpdateUseCaseParams(
    val imei: String,
    val isFromWeb: Boolean,
    val updateStepId: Int,
)
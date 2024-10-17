
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.models.DeviceIdRequestModel
import com.luminsoft.enroll_sdk.core.utils.UseCase

class LostDeviceIdUseCase(private val lostDeviceIdRepository: LostDeviceIdRepository) :
    UseCase<Either<SdkFailure, Null>, LostDeviceIdUseCaseParams> {
    override suspend fun call(params: LostDeviceIdUseCaseParams): Either<SdkFailure, Null> {
        val deviceIdRequestModel = DeviceIdRequestModel()
        deviceIdRequestModel.deviceId = params.deviceId
        deviceIdRequestModel.deviceModel = params.deviceModel
        deviceIdRequestModel.manufacturerName = params.manufacturerName

        return lostDeviceIdRepository.lostDeviceId(deviceIdRequestModel)
    }

}

data class LostDeviceIdUseCaseParams(
    val deviceId: String,
    val deviceModel: String,
    val manufacturerName: String,
)


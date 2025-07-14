
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class UpdateDeviceIdUseCase(private val updateDeviceIdRepository: UpdateDeviceIdRepository) :
    UseCase<Either<SdkFailure, Null>, UpdateDeviceIdUseCaseParams> {
    override suspend fun call(params: UpdateDeviceIdUseCaseParams): Either<SdkFailure, Null> {
        val deviceIdRequestModel = UpdateDeviceIdRequestModel()
        deviceIdRequestModel.deviceId = params.deviceId
        deviceIdRequestModel.deviceModel = params.deviceModel
        deviceIdRequestModel.manufacturerName = params.manufacturerName

        return updateDeviceIdRepository.updateDeviceId(deviceIdRequestModel)
    }

}

data class UpdateDeviceIdUseCaseParams(
    val deviceId: String,
    val deviceModel: String,
    val manufacturerName: String,
)


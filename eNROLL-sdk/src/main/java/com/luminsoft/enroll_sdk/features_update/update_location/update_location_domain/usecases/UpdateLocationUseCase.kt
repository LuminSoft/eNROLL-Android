
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class UpdateLocationUseCase(private val locationRepository: UpdateLocationRepository) :
    UseCase<Either<SdkFailure, Null>, UpdateLocationUseCaseParams> {

    override suspend fun call(params: UpdateLocationUseCaseParams): Either<SdkFailure, Null> {
        val updateLocationRequest = UpdateLocationRequestModel()
        updateLocationRequest.latitude = params.latitude
        updateLocationRequest.longitude = params.longitude
        return locationRepository.updateLocation(updateLocationRequest)
    }
}


data class UpdateLocationUseCaseParams(
    val latitude: Double,
    val longitude: Double,
)
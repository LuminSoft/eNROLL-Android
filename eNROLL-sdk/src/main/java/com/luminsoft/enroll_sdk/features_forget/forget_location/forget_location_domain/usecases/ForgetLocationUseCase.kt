
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class ForgetLocationUseCase(private val locationRepository: ForgetLocationRepository) :
    UseCase<Either<SdkFailure, Null>, ForgetLocationUseCaseParams> {

    override suspend fun call(params: ForgetLocationUseCaseParams): Either<SdkFailure, Null> {
        val postLocationRequest = ForgetLocationRequestModel()
        postLocationRequest.latitude = params.latitude
        postLocationRequest.longitude = params.longitude
        return locationRepository.forgetLocation(postLocationRequest)
    }
}


data class ForgetLocationUseCaseParams(
    val latitude: Double,
    val longitude: Double,
)
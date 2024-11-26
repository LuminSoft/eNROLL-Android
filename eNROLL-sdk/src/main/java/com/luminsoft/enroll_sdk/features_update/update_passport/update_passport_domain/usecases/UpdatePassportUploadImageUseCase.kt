
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class UpdatePassportUploadImageUseCase(private val updatePassportRepository: UpdatePassportRepository) :
    UseCase<Either<SdkFailure, UpdatePassportCustomerData>, UpdatePassportUploadImageUseCaseParams> {

    override suspend fun call(params: UpdatePassportUploadImageUseCaseParams): Either<SdkFailure, UpdatePassportCustomerData> {
        val updatePassportUploadImageRequest = UpdatePassportUploadImageRequest()
        updatePassportUploadImageRequest.image = params.image

        return updatePassportRepository.updatePassportUploadImage(
            updatePassportUploadImageRequest
        )
    }
}

data class UpdatePassportUploadImageUseCaseParams(
    val image: String,
)
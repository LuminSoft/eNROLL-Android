
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface UpdateNationalIdConfirmationRepository {
    suspend fun updatePersonalConfirmationUploadImage(request: UpdatePersonalConfirmationUploadImageRequest): Either<SdkFailure, UpdateCustomerData>
    suspend fun updatePersonalConfirmationApprove(request: UpdatePersonalConfirmationApproveRequest): Either<SdkFailure, Null>
    suspend fun isTranslationStepEnabled(): Either<SdkFailure, IsTranslationEnabledResponse>
}
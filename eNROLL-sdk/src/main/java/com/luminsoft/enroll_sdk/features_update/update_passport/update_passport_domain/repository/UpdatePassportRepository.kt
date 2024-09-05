
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface UpdatePassportRepository {
    suspend fun updatePassportUploadImage(request: UpdatePassportUploadImageRequest): Either<SdkFailure, UpdatePassportCustomerData>
    suspend fun updatePassportApprove(request: UpdatePassportApproveRequest): Either<SdkFailure, Null>
    suspend fun isTranslationStepEnabled(): Either<SdkFailure, IsTranslationEnabledResponse>
    }
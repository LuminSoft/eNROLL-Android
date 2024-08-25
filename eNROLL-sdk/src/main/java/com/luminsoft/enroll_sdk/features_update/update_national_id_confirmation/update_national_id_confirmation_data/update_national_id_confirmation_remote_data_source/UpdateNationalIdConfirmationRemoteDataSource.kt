
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface  UpdateNationalIdConfirmationRemoteDataSource  {
    suspend fun updatePersonalConfirmationUploadImage(request: UpdatePersonalConfirmationUploadImageRequest): BaseResponse<Any>
    suspend fun updatePersonalConfirmationApprove(request: UpdatePersonalConfirmationApproveRequest): BaseResponse<Any>
    suspend fun isTranslationStepEnabled(): BaseResponse<Any>
}
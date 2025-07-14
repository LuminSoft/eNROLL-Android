
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface  UpdatePassportRemoteDataSource  {
    suspend fun updatePassportUploadImage(request: UpdatePassportUploadImageRequest): BaseResponse<Any>
    suspend fun updatePassportApprove(request: UpdatePassportApproveRequest): BaseResponse<Any>
    suspend fun isTranslationStepEnabled(): BaseResponse<Any>

}
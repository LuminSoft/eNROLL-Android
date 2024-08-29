
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface MailAuthUpdateRemoteDataSource {
    suspend fun sendMailAuthUpdateOtp(updateStepId: Int): BaseResponse<Any>
    suspend fun validateOTPMailAuthUpdate(request: ValidateOTPAuthUpdateRequestModel): BaseResponse<Any>

}

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class MailAuthUpdateRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val mailApi: MailAuthUpdateApi
) :
    MailAuthUpdateRemoteDataSource {
    override suspend fun sendMailAuthUpdateOtp(updateStepId: Int): BaseResponse<Any> {
        return network.apiRequest { mailApi.sendMailAuthUpdateOtp(updateStepId) }
    }

    override suspend fun validateOTPMailAuthUpdate(request: ValidateOTPAuthUpdateRequestModel): BaseResponse<Any> {
        return network.apiRequest { mailApi.validateOTPMailAuthUpdate(request) }
    }
}







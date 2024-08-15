
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import retrofit2.http.Body

interface SecurityQuestionAuthUpdateRemoteDataSource {
    suspend fun getSecurityQuestion(updateStepId: Int): BaseResponse<Any>
    suspend fun validateSecurityQuestion(@Body request: SecurityQuestionAuthUpdateRequestModel): BaseResponse<Any>
}
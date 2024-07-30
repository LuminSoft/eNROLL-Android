
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import retrofit2.http.Body

interface SecurityQuestionAuthRemoteDataSource {
    suspend fun getSecurityQuestion(): BaseResponse<Any>
    suspend fun validateSecurityQuestion(@Body request: SecurityQuestionAuthRequestModel): BaseResponse<Any>
}
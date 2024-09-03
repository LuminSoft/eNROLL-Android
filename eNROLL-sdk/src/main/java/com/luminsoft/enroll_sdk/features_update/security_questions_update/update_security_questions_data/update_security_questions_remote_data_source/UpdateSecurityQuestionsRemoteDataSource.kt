
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import retrofit2.http.Body

interface UpdateSecurityQuestionsRemoteDataSource {
    suspend fun getSecurityQuestions(): BaseResponse<Any>
    suspend fun postSecurityQuestions(@Body request: List<SecurityQuestionsUpdateRequestModel>): BaseResponse<Any>
}
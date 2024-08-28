
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth_update.password_auth_update.password_auth_update_data.password_auth_update_models.SetPasswordAuthUpdateRequest

interface PasswordAuthUpdateRemoteDataSource {
    suspend fun verifyPassword(request: SetPasswordAuthUpdateRequest): BaseResponse<Any>
}
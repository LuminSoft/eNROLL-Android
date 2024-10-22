
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth_update.password_auth_update.password_auth_update_data.password_auth_update_models.SetPasswordAuthUpdateRequest


class PasswordAuthUpdateRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val passwordApi: PasswordAuthUpdateApi
) :
    PasswordAuthUpdateRemoteDataSource {
    override suspend fun verifyPassword(request: SetPasswordAuthUpdateRequest): BaseResponse<Any> {
        return network.apiRequest { passwordApi.verifyPassword(request) }
    }
}







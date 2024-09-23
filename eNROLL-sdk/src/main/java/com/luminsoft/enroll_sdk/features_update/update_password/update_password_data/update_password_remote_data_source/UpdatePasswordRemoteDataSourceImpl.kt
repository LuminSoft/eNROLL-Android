
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class UpdatePasswordRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val passwordApi: UpdatePasswordApi
) :
    UpdatePasswordRemoteDataSource {

    override suspend fun updatePassword(request: UpdatePasswordRequest): BaseResponse<Any> {
        return network.apiRequest { passwordApi.updatePassword(request) }
    }
}







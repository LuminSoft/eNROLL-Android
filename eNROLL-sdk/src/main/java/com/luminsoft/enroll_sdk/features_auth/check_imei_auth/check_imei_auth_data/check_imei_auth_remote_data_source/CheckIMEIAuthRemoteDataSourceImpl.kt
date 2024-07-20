import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class CheckIMEIAuthRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val checkIMEI: CheckIMEIAuthApi
) :
    CheckIMEIAuthRemoteDataSource {
    override suspend fun checkIMEIAuth(request: CheckIMEIRequestModel): BaseResponse<Any> {
        return network.apiRequest { checkIMEI.checkIMEIAuth(request) }
    }


}













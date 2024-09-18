
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class ForgetLocationRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val locationApi: ForgetLocationApi
) :
    ForgetLocationRemoteDataSource {

    override suspend fun forgetLocation(request: ForgetLocationRequestModel): BaseResponse<Any> {

        return network.apiRequest { locationApi.forgetLocation(request) }

    }
}







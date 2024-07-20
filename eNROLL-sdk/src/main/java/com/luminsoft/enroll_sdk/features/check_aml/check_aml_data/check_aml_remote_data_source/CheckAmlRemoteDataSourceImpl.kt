
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class CheckAmlRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val checkAmlApi: CheckAmlApi
) :
    CheckAmlRemoteDataSource {

    override suspend fun checkAml(): BaseResponse<Any> {
        return network.apiRequest { checkAmlApi.checkAml() }
    }
}







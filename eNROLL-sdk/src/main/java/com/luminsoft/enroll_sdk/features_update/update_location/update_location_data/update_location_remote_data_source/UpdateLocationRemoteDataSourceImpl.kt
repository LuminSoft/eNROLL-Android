
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class UpdateLocationRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val locationApi: UpdateLocationApi
) :
    UpdateLocationRemoteDataSource {

    override suspend fun updateLocation(request: UpdateLocationRequestModel): BaseResponse<Any> {

        return network.apiRequest { locationApi.updateLocation(request) }

    }
}







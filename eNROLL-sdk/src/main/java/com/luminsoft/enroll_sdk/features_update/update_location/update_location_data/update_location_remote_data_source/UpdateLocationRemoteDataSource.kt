
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel

interface  UpdateLocationRemoteDataSource  {
    suspend fun updateLocation(request: UpdateLocationRequestModel): BaseResponse<Any>
}

import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface  UpdatePasswordRemoteDataSource  {
    suspend fun updatePassword(request: UpdatePasswordRequest): BaseResponse<Any>
}
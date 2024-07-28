
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface  CheckAmlRemoteDataSource  {
    suspend fun checkAml(): BaseResponse<Any>
}
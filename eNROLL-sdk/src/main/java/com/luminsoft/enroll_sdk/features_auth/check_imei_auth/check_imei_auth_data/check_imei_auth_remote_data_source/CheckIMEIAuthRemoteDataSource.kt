import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface CheckIMEIAuthRemoteDataSource {
    suspend fun checkIMEIAuth(request: CheckIMEIRequestModel): BaseResponse<Any>
}


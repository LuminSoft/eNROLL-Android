
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.check_imei_auth_remote_data_source.CheckIMEIAuthRemoteDataSource

class CheckIMEIAuthRepositoryImplementation(private val checkIMEIRemoteDataSource: CheckIMEIAuthRemoteDataSource) :
    CheckIMEIAuthRepository {
    override suspend fun checkIMEIAuth(request: CheckIMEIRequestModel): Either<SdkFailure, Null> {
        return when (val response = checkIMEIRemoteDataSource.checkIMEIAuth(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }


}






import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_models.CheckAmlResponseModel

class CheckAmlRepositoryImplementation(private val checkAmlRemoteDataSource: CheckAmlRemoteDataSource) :
    CheckAmlRepository {
    override suspend fun checkAml(): Either<SdkFailure, CheckAmlResponseModel> {
        return when (val response = checkAmlRemoteDataSource.checkAml()) {
            is BaseResponse.Success -> {
                val responseObject = response.data as CheckAmlResponseModel
                Either.Right(responseObject)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}

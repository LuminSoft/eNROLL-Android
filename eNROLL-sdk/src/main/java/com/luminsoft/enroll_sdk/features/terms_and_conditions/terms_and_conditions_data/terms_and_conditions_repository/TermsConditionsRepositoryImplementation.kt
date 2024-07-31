import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.AcceptTermsRequestModel
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.TermsIdResponseModel
import okhttp3.ResponseBody

class TermsConditionsRepositoryImplementation(private val termsConditionsRemoteDataSource: TermsConditionsRemoteDataSource) :
    TermsConditionsRepository {


    override suspend fun getTermsId(): Either<SdkFailure, TermsIdResponseModel> {
        return when (val response = termsConditionsRemoteDataSource.getTermsId()) {

            is BaseResponse.Success -> {
                val termsResponse = response.data as TermsIdResponseModel
                Either.Right(termsResponse)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun getTermsPdfFileById(id: Int): Either<SdkFailure, ResponseBody> {


        return when (val response = termsConditionsRemoteDataSource.getTermsPdfFileById(id)) {

            is BaseResponse.Success -> {

                val responseBody = response.data as ResponseBody
                Either.Right(responseBody)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }

    }

    override suspend fun acceptTerms(request: AcceptTermsRequestModel): Either<SdkFailure, Null> {

        return when (val response = termsConditionsRemoteDataSource.acceptTerms(request)) {

            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}

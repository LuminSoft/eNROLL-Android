

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_models.InsertSignatureInfoRequestModel

class ElectronicSignatureRepositoryImplementation(private val electronicSignatureRemoteDataSource: ElectronicSignatureRemoteDataSource) :
    ElectronicSignatureRepository {

    override suspend fun insertSignatureInfo(request: InsertSignatureInfoRequestModel): Either<SdkFailure, Null> {
        return when (val response = electronicSignatureRemoteDataSource.insertElectronicSignatureInfo(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun hasNationalId(): Either<SdkFailure, Boolean> {
        return when (val response = electronicSignatureRemoteDataSource.hasNationalId()) {
            is BaseResponse.Success -> {
                val userHasNationalId = response.data as Boolean
                Either.Right(userHasNationalId)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}




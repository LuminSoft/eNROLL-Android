
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class UpdateNationalIdConfirmationRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val nationalIdConfirmationApi: UpdateNationalIdConfirmationApi
) :
    UpdateNationalIdConfirmationRemoteDataSource {

    override suspend fun updatePersonalConfirmationUploadImage(request: UpdatePersonalConfirmationUploadImageRequest): BaseResponse<Any> {
        return when (request.scanType) {
            UpdateScanType.FRONT -> network.apiRequest {
                nationalIdConfirmationApi.updateNationalIdUploadFrontImage(
                    request
                )
            }

            UpdateScanType.Back -> network.apiRequest {
                nationalIdConfirmationApi.updateNationalIdUploadBackImage(
                    request
                )
            }

        }
    }

    override suspend fun updatePersonalConfirmationApprove(request: UpdatePersonalConfirmationApproveRequest): BaseResponse<Any> {
        return when (request.scanType) {
            UpdateScanType.FRONT -> network.apiRequest {
                nationalIdConfirmationApi.updateNationalIdApproveFront(
                    request
                )
            }

            UpdateScanType.Back -> network.apiRequest {
                nationalIdConfirmationApi.updateNationalIdApproveBackImage(
                    request
                )
            }

        }
    }

    override suspend fun isTranslationStepEnabled(): BaseResponse<Any> {
        return network.apiRequest { nationalIdConfirmationApi.isTranslationStepEnabled() }
    }
}






package com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_repository


import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_domain.repository.PasswordRepository
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_remote_data_source.PasswordRemoteDataSource

class PasswordRepositoryImplementation(private val passwordRemoteDataSource: PasswordRemoteDataSource):
    PasswordRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = passwordRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}


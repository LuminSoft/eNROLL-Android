package com.luminsoft.ekyc_android_sdk.features.location.location_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.utils.UseCase
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel
import com.luminsoft.ekyc_android_sdk.features.location.location_domain.repository.LocationRepository

class PostLocationUseCase(private val locationRepository: LocationRepository) :
    UseCase<Either<SdkFailure, Null>, PostLocationUseCaseParams> {

    override suspend fun call(params: PostLocationUseCaseParams): Either<SdkFailure, Null> {
        val postLocationRequest = PostLocationRequestModel()
        postLocationRequest.latitude = params.latitude
        postLocationRequest.longitude = params.longitude
        return locationRepository.postLocation(postLocationRequest)
    }
}


data class PostLocationUseCaseParams(
    val latitude: Double,
    val longitude: Double,
)
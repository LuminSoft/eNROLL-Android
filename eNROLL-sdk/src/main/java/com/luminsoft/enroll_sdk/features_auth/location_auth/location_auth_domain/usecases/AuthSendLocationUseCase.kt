package com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_domain.repository.LocationAuthRepository

class PostLocationAuthUseCase(private val locationRepository: LocationAuthRepository) :
    UseCase<Either<SdkFailure, Null>, PostLocationAuthUseCaseParams> {

    override suspend fun call(params: PostLocationAuthUseCaseParams): Either<SdkFailure, Null> {
        val postLocationRequest = PostLocationRequestModel()
        postLocationRequest.latitude = params.latitude
        postLocationRequest.longitude = params.longitude
        return locationRepository.postAuthLocation(postLocationRequest)
    }
}


data class PostLocationAuthUseCaseParams(
    val latitude: Double,
    val longitude: Double,
)
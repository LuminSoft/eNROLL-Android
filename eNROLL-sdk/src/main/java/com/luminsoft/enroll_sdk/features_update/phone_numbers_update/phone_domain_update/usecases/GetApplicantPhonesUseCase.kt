package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository.PhoneRepositoryUpdate

class GetApplicantPhonesUseCase(private val mailsRepository: PhoneRepositoryUpdate) :
    UseCase<Either<SdkFailure, List<GetVerifiedPhonesResponseModel>>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, List<GetVerifiedPhonesResponseModel>> {
        return mailsRepository.getApplicantPhones()
    }
}


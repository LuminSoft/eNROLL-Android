package com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_domain.usecases
import TermsConditionsRepository
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.AcceptTermsRequestModel

class AcceptTermsUseCase (private val termsConditionsRepository: TermsConditionsRepository) :
    UseCase<Either<SdkFailure, Null>, AcceptTermsRequestModel> {


    override suspend fun call(params: AcceptTermsRequestModel): Either<SdkFailure, Null> {
        return termsConditionsRepository.acceptTerms(params)
    }
}



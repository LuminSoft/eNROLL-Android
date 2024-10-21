package com.luminsoft.enroll_sdk.main.main_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_applicatnt_id.GetApplicantIdResponse
import com.luminsoft.enroll_sdk.main.main_domain.repository.MainRepository


class GetApplicantIdUsecase(private  val mainRepository: MainRepository):
    UseCase<Either<SdkFailure, GetApplicantIdResponse> ,Null> {

    override suspend fun call(params: Null): Either<SdkFailure, GetApplicantIdResponse> {
       return mainRepository.getApplicantId()
    }
}


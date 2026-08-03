package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.repository.QuestionnaireRepository

class InitializeQuestionnaireRequestUseCase(
    private val questionnaireRepository: QuestionnaireRepository
) : UseCase<Either<SdkFailure, InitializeQuestionnaireResponse>, InitializeQuestionnaireRequestUseCaseParams> {
    override suspend fun call(
        params: InitializeQuestionnaireRequestUseCaseParams
    ): Either<SdkFailure, InitializeQuestionnaireResponse> {
        return questionnaireRepository.initializeRequest(InitializeQuestionnaireRequest())
    }
}

class InitializeQuestionnaireRequestUseCaseParams

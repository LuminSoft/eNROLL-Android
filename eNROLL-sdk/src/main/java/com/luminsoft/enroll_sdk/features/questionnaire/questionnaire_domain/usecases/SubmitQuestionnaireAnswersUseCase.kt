package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireAnswerRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.repository.QuestionnaireRepository

class SubmitQuestionnaireAnswersUseCase(
    private val questionnaireRepository: QuestionnaireRepository
) : UseCase<Either<SdkFailure, Null>, List<QuestionnaireAnswerRequest>> {
    override suspend fun call(params: List<QuestionnaireAnswerRequest>): Either<SdkFailure, Null> {
        return questionnaireRepository.submitQuestions(params)
    }
}

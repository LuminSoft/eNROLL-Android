package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireQuestionModel
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.repository.QuestionnaireRepository

class GetQuestionnaireQuestionsUseCase(
    private val questionnaireRepository: QuestionnaireRepository
) : UseCase<Either<SdkFailure, List<QuestionnaireQuestionModel>>, Null> {
    override suspend fun call(params: Null): Either<SdkFailure, List<QuestionnaireQuestionModel>> {
        return questionnaireRepository.getQuestions()
    }
}

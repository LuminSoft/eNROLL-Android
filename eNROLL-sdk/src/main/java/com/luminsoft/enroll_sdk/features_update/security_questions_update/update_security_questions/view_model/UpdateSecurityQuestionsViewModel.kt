
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_onboarding.ui.components.answerValidate
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class UpdateSecurityQuestionsViewModel(
    private val getSecurityQuestionsUseCase: GetSecurityQuestionsUpdateUseCase,
    private val postSecurityQuestionsUseCase: UpdateSecurityQuestionsUseCase,
    private val updateViewModel: UpdateViewModel,

    ) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var securityQuestionsApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)

    var selectQuestionError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var answerError: MutableStateFlow<String?> = MutableStateFlow(null)

    var selectedQuestion: MutableStateFlow<GetSecurityQuestionsUpdateResponseModel?> =
        MutableStateFlow(null)
    var answer: MutableStateFlow<TextFieldValue> =
        MutableStateFlow(TextFieldValue())

    init {
        if (updateViewModel.securityQuestions.value == null)
            getSecurityQuestions()
    }

    fun postSecurityQuestionsCall() {
        updateSecurityQuestions()
    }

    fun getSecurityQuestions() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, List<GetSecurityQuestionsUpdateResponseModel>> =
                getSecurityQuestionsUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let {
                        updateViewModel.securityQuestions.value = s
                        updateViewModel.securityQuestionsList.value = ArrayList(s)
                    }
                    loading.value = false
                })
        }
    }

    private fun updateSecurityQuestions() {
        loading.value = true
        ui {
            val item = SecurityQuestionsUpdateRequestModel()
            item.securityQuestionId = updateViewModel.selectedSecurityQuestions.value[0].id
            item.answer = updateViewModel.selectedSecurityQuestions.value[0].answer

            val item1 = SecurityQuestionsUpdateRequestModel()
            item1.securityQuestionId = updateViewModel.selectedSecurityQuestions.value[1].id
            item1.answer = updateViewModel.selectedSecurityQuestions.value[1].answer

            val item2 = SecurityQuestionsUpdateRequestModel()
            item2.securityQuestionId = updateViewModel.selectedSecurityQuestions.value[2].id
            item2.answer = updateViewModel.selectedSecurityQuestions.value[2].answer

            val listToSent = listOf(item, item1, item2)

            val response: Either<SdkFailure, Null> =
                postSecurityQuestionsUseCase.call(listToSent)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    securityQuestionsApproved.value = true
                })
        }
    }


    fun onChangeValue(textFieldValue: TextFieldValue) {
        answer.value = textFieldValue
        answerValidation(textFieldValue.text)
    }

    private fun answerValidation(value: String) = when {

        !answerValidate.value
        -> {
            answerError.value = null
        }

        value.isEmpty() -> {
            answerError.value =
                ResourceProvider.instance.getStringResource(R.string.errorEmptyAnswer)
        }

        value.length > 150 -> {
            answerError.value =
                ResourceProvider.instance.getStringResource(R.string.errorMaxLengthAnswer)
        }


        else -> answerError.value = null
    }
}
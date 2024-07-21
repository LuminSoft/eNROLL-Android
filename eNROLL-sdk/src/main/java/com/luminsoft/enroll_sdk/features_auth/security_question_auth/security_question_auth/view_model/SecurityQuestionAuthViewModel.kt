
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_onboarding.ui.components.answerValidate
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SecurityQuestionAuthViewModel(
    private val getSecurityQuestionAuthUseCase: GetSecurityQuestionAuthUseCase,
    private val validateSecurityQuestionUseCase: ValidateSecurityQuestionUseCase,
    private val authViewModel: AuthViewModel,

    ) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var securityQuestionApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)

    var selectQuestionError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var answerError: MutableStateFlow<String?> = MutableStateFlow(null)

    var securityQuestion: MutableStateFlow<GetSecurityQuestionAuthResponseModel?> = MutableStateFlow(null)

    //    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)

    var selectedQuestion: MutableStateFlow<GetSecurityQuestionAuthResponseModel?> =
        MutableStateFlow(null)

    var answer: MutableStateFlow<TextFieldValue> =
        MutableStateFlow(TextFieldValue())



    init {
        if (securityQuestion.value == null)
            getSecurityQuestion()
    }

/*
    fun postSecurityQuestionsCall() {
        postSecurityQuestions()
    }
*/

    fun getSecurityQuestion() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, GetSecurityQuestionAuthResponseModel> =
                getSecurityQuestionAuthUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let {
                        securityQuestion.value = s
                    }
                    loading.value = false
                })
        }
    }

 /*   private fun postSecurityQuestions() {
        loading.value = true
        ui {
            val item = SecurityQuestionsRequestModel()
            item.securityQuestionId = onBoardingViewModel.selectedSecurityQuestions.value[0].id
            item.answer = onBoardingViewModel.selectedSecurityQuestions.value[0].answer

            val item1 = SecurityQuestionsRequestModel()
            item1.securityQuestionId = onBoardingViewModel.selectedSecurityQuestions.value[1].id
            item1.answer = onBoardingViewModel.selectedSecurityQuestions.value[1].answer

            val item2 = SecurityQuestionsRequestModel()
            item2.securityQuestionId = onBoardingViewModel.selectedSecurityQuestions.value[2].id
            item2.answer = onBoardingViewModel.selectedSecurityQuestions.value[2].answer

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
//                    loading.value = false
                })
        }
    }

*/

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

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow

class SecurityQuestionAuthViewModel(
    private val getSecurityQuestionAuthUseCase: GetSecurityQuestionAuthUseCase,
    private val validateSecurityQuestionUseCase: ValidateSecurityQuestionUseCase,

    ) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var securityQuestionApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)

    var answerError: MutableStateFlow<String?> = MutableStateFlow(null)

    var securityQuestion: MutableStateFlow<GetSecurityQuestionAuthResponseModel?> =
        MutableStateFlow(null)


    var answer: MutableStateFlow<TextFieldValue> =
        MutableStateFlow(TextFieldValue())


    init {
        if (securityQuestion.value == null)
            getSecurityQuestion()
    }

    fun validateSecurityQuestionsCall() {
        validateSecurityQuestionsAnswer()
    }

    private fun getSecurityQuestion() {
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


    private fun validateSecurityQuestionsAnswer() {
        loading.value = true
        ui {

            val securityAnswerModel = SecurityQuestionAuthRequestModel()
            securityAnswerModel.answer = answer.value.text
            securityAnswerModel.securityQuestionId = securityQuestion.value?.id

            val response: Either<SdkFailure, Null> =
                validateSecurityQuestionUseCase.call(securityAnswerModel)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    securityQuestionApproved.value = true
                    loading.value = false
                })
        }
    }


    fun onChangeValue(textFieldValue: TextFieldValue) {

        answer.value = textFieldValue
        answerValidation(textFieldValue.text.trim())
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
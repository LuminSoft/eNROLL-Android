
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class SecurityQuestionAuthUpdateViewModel(
    private val getSecurityQuestionAuthUseCase: GetSecurityQuestionAuthUpdateUseCase,
    private val validateSecurityQuestionUseCase: ValidateSecurityQuestionAuthUpdateUseCase) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var securityQuestionApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var answerError: MutableStateFlow<String?> = MutableStateFlow(null)
    var securityQuestion: MutableStateFlow<GetSecurityQuestionAuthUpdateResponseModel?> =
        MutableStateFlow(null)
    var answer: MutableStateFlow<TextFieldValue> =
        MutableStateFlow(TextFieldValue())

    private val _stepUpdateId = MutableStateFlow<Int?>(null)
    private val stepUpdateId: StateFlow<Int?> get() = _stepUpdateId



    init {
        viewModelScope.launch {
            stepUpdateId
                .filterNotNull() // Ignore null values
                .collect { id ->
                    if (securityQuestion.value == null)
                        getSecurityQuestion(id)
                }
        }

    }


    fun setStepUpdateId(id: Int) {
        _stepUpdateId.value = id
    }
    fun validateSecurityQuestionsCall() {
        validateSecurityQuestionsAnswer()
    }

    private fun getSecurityQuestion(updateStepId:Int) {
        viewModelScope.launch {
            loading.value = true
            ui {
                val response: Either<SdkFailure, GetSecurityQuestionAuthUpdateResponseModel> =
                    getSecurityQuestionAuthUseCase.call(updateStepId)

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
    }


    private fun validateSecurityQuestionsAnswer() {
        loading.value = true
        ui {

            val securityAnswerModel = SecurityQuestionAuthUpdateRequestModel()
            securityAnswerModel.answer = answer.value.text
            securityAnswerModel.securityQuestionId = securityQuestion.value?.id
            securityAnswerModel.updateStep = _stepUpdateId.value

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

        !isAnswerValidate.value
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
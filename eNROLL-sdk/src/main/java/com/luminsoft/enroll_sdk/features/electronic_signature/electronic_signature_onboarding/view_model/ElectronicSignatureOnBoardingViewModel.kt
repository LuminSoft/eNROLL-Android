
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow


@Suppress("DEPRECATION")
class ElectronicSignatureOnBoardingViewModel(
    private val electronicSignatureUseCase: InsertSignatureInfoUseCase,
    private val checkUserHasNationalIdUseCase: CheckUserHasNationalIdUseCase,
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)



    var chosenStep: MutableStateFlow<ElectronicSignatureChooseStep?> =
        MutableStateFlow(ElectronicSignatureChooseStep.ApplyForSignature)
    var isStepSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var currentPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentPhoneNumberCode: MutableStateFlow<String?> = MutableStateFlow("+20")
    val phoneNumberShowError: MutableState<Boolean> = mutableStateOf(false)

    var emailValue: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue())
    var emailError: MutableState<String?> = mutableStateOf(null)

    var nationalIdValue: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue())
    var nationalIdError: MutableState<String?> = mutableStateOf(null)

    var phoneNumberValue: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue())
    var phoneNumberError: MutableState<String?> = mutableStateOf(null)

    val userHasModifiedText: MutableState<Boolean> = mutableStateOf(false)



    val userHasModifiedEmail: MutableState<Boolean> = mutableStateOf(false)

    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)

    var skippedSucceed: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    var haveSignatureSucceed: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    var applySignatureSucceed: MutableStateFlow<Boolean?> = MutableStateFlow(false)

    var failedStatus: MutableStateFlow<Int?> = MutableStateFlow(null)

    var userHasNationalId: MutableStateFlow<Boolean?> = MutableStateFlow(false)


    init {
        checkUserHasNationalId()
    }




private fun handleSuccess(status: Int) {
    loading.value = false
    when (status) {
        1 -> {
            haveSignatureSucceed.value = true
        }
        2 -> {
            applySignatureSucceed.value = true
        }
        3 -> {
            skippedSucceed.value = true
        }
       else -> {

        }
    }
}

     fun insertSignatureInfo(status: Int,nationalId: String,phoneNumber: String ,email: String ) {
        loading.value = true
        ui {

            params.value = ApplySignatureUseCaseParams(
                status,
                nationalId,
                phoneNumber,
                email,

            )

            val response: Either<SdkFailure, Null> = electronicSignatureUseCase.
            call(params.value as ApplySignatureUseCaseParams)

            response.fold(
                {
                    failedStatus.value = status
                    failure.value = it
                    loading.value = false

                },
                {
                    handleSuccess(status)
                })
        }
    }



     private fun checkUserHasNationalId() {
        loading.value = true
        ui {

            val response: Either<SdkFailure, Boolean> = checkUserHasNationalIdUseCase.
            call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    println("testtt $it")
                    userHasNationalId.value = it
                    loading.value = false

                })
        }
    }






}


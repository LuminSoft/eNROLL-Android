import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_navigation.phoneScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.common.MainViewModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_auth_configurations.StepSignContractModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GenerateSignContractSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GenerateSignContractSessionTokenUsecaseParams
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.InitializeRequestSignContractUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.InitializeRequestSignContractUsecaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class SignContractViewModel(
    private val generateSignContractSessionToken: GenerateSignContractSessionTokenUsecase,
    private val initializeRequestUsecase: InitializeRequestSignContractUsecase,
    private val context: Context

) : ViewModel(),
    MainViewModel {
    override var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    override var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    override var token: MutableStateFlow<String?> = MutableStateFlow(null)
    var customerId: MutableStateFlow<String?> = MutableStateFlow(null)
    var errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var mailValue: MutableStateFlow<TextFieldValue?> = MutableStateFlow(TextFieldValue())
    var steps: MutableStateFlow<List<StepSignContractModel>?> = MutableStateFlow(null)
    var navController: NavController? = null
    var smileImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdFrontImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var passportImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdBackImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var scanType: MutableStateFlow<ScanType?> = MutableStateFlow(null)
    var securityQuestions: MutableStateFlow<List<GetSecurityQuestionsResponseModel>?> =
        MutableStateFlow(null)

    override fun retry(navController: NavController) {
        TODO("Not yet implemented")
    }


    fun initRequest() {
        loading.value = true
        ui {

            val deviceId = DeviceIdentifier.getDeviceId(context)
            val manufacturer: String = Build.MANUFACTURER
            val deviceModel: String = Build.MODEL

            params.value = InitializeRequestSignContractUsecaseParams(
                deviceId,
                manufacturer,
                deviceModel
            )
            val response: Either<SdkFailure, Null> =
                initializeRequestUsecase.call(params.value as InitializeRequestSignContractUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {

                    loading.value = false
                    navigateToNextStep()
                })

        }
    }

    fun enableLoading() {
        loading.value = true
    }

    fun disableLoading() {
        loading.value = false
    }

    init {
        generateToken()
    }

    private fun generateToken() {
        loading.value = true
        ui {
            params.value = GenerateSignContractSessionTokenUsecaseParams(
                EnrollSDK.tenantId,
                EnrollSDK.tenantSecret,
                EnrollSDK.applicantId,
                EnrollSDK.contractTemplateId,
                EnrollSDK.contractParameters,
            )

            val response: Either<SdkFailure, String> =
                generateSignContractSessionToken.call(params.value as GenerateSignContractSessionTokenUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        token.value = it1
                        RetroClient.setToken(it1)
                        initRequest()
                    }
                })
        }
    }

    fun removeCurrentStep(id: Int): Boolean {
        // Check if 'steps' is not null
        if (steps.value != null) {
            // Store the initial size of the 'steps' list
            val stepsSize = steps.value!!.size

            // Create a mutable list from 'steps', remove the item with the given ID, and convert back to an immutable list
            steps.value = steps.value!!.toMutableList().apply {
                removeIf { x -> x.authenticationStepId == id }
            }.toList()

            // Store the new size of the 'steps' list after removal
            val newStepsSize = steps.value!!.size

            // Check if the size of the list has changed (i.e., an item was removed)
            if (stepsSize != newStepsSize) {
                // If the list is not empty, navigate to the next step and return 'false'
                return if (steps.value!!.isNotEmpty()) {
                    navigateToNextStep()
                    false
                } else
                    true
            } else if (newStepsSize == 0)
                return true
        }
        return false
    }

    private fun navigateToNextStep() {
        mailValue.value = TextFieldValue()
        currentPhoneNumber.value = null
        navController!!.navigate(phoneScreenContent)
    }
}
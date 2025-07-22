package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_presentation.main_sign_contract.view_model

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
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_navigation.currentContractLowRiskFRAScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.common.MainViewModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_steps.ContractFileModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_steps.StepSignContractModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GenerateSignContractSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GenerateSignContractSessionTokenUsecaseParams
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GetSignContractStepsUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GetSignContractStepsUsecaseParams
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.InitializeRequestSignContractUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.InitializeRequestSignContractUsecaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class SignContractViewModel(
    private val generateSignContractSessionToken: GenerateSignContractSessionTokenUsecase,
    private val initializeRequestUsecase: InitializeRequestSignContractUsecase,
    private val getSignContractStepsUsecase: GetSignContractStepsUsecase,
    private val context: Context

) : ViewModel(),
    MainViewModel {
    override var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    override var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    override var token: MutableStateFlow<String?> = MutableStateFlow(null)
    var contractId: MutableStateFlow<String?> = MutableStateFlow(null)
    var contractVersionNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentStepIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    var contractFileModelList: MutableStateFlow<ArrayList<ContractFileModel>?> =
        MutableStateFlow(null)
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
    var getCurrentContract: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var showAllContracts: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun retry(navController: NavController) {
        TODO("Not yet implemented")
    }


    private fun initRequest() {
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
                    getSignContractSteps()
                })

        }
    }

    private fun getSignContractSteps() {
        loading.value = true
        ui {

            params.value = GetSignContractStepsUsecaseParams()
            val response: Either<SdkFailure, StepSignContractModel> =
                getSignContractStepsUsecase.call(params.value as GetSignContractStepsUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { res ->
                    response.let {
                        contractId.value = res.contractId.toString()
                        contractVersionNumber.value = res.contractVersionNumber.toString()
                        contractFileModelList.value = res.contractVersionDetailModel
                        currentStepIndex.value = 0
                        loading.value = false
                        navigateToNextStep()
                    }

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

    private fun navigateToNextStep() {
        mailValue.value = TextFieldValue()
        currentPhoneNumber.value = null
        navController!!.navigate(currentContractLowRiskFRAScreenContent)
    }

    fun getNextContract() {
        if ((currentStepIndex.value + 1) == contractFileModelList.value!!.size) {
            getCurrentContract.value = false
            showAllContracts.value = true
        } else {
            currentStepIndex.value++
            getCurrentContract.value = true
        }
    }

    fun getContractText(): String {
        return contractFileModelList.value!![currentStepIndex.value].signContractTextEnum!!
    }
}
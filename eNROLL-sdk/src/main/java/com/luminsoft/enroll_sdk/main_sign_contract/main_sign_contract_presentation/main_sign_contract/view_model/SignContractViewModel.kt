package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_presentation.main_sign_contract.view_model

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.models.EnrollContractSignatureMode
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_navigation.currentContractLowRiskFRAScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.common.MainViewModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_files.SignContractFileItemModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_steps.ContractFileModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_steps.StepSignContractModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GenerateSignContractSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GenerateSignContractSessionTokenUsecaseParams
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GetSignContractFilesUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GetSignContractFilesUsecaseParams
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GetSignContractStepsUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GetSignContractStepsUsecaseParams
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.InitializeRequestSignContractUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.InitializeRequestSignContractUsecaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class SignContractViewModel(
    private val generateSignContractSessionToken: GenerateSignContractSessionTokenUsecase,
    private val initializeRequestUsecase: InitializeRequestSignContractUsecase,
    private val getSignContractStepsUsecase: GetSignContractStepsUsecase,
    private val getSignContractFilesUsecase: GetSignContractFilesUsecase,
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

    var isMultiSigning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var signContractFiles: MutableStateFlow<List<SignContractFileItemModel>?> = MutableStateFlow(null)
    var currentContractFileIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    var currentRequestId: MutableStateFlow<String?> = MutableStateFlow(null)

    override fun retry(navController: NavController) {
        this.navController = navController
        failure.value = null
        token.value = null
        contractId.value = null
        contractVersionNumber.value = null
        contractFileModelList.value = null
        currentStepIndex.value = 0
        getCurrentContract.value = false
        showAllContracts.value = false
        isMultiSigning.value = false
        signContractFiles.value = null
        currentContractFileIndex.value = 0
        currentRequestId.value = null
        generateToken()
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
                    if (EnrollSDK.signContractMode == EnrollContractSignatureMode.LOW_RISK_FRA) {
                        if (isMultiSigning.value) {
                            getSignContractFiles()
                        } else {
                            getSignContractSteps()
                        }
                    } else {
                        loading.value = false
                        navigateToNextStep()
                    }
                })

        }
    }

    private fun getSignContractFiles() {
        loading.value = true
        ui {
            params.value = GetSignContractFilesUsecaseParams()
            val response: Either<SdkFailure, List<SignContractFileItemModel>> =
                getSignContractFilesUsecase.call(params.value as GetSignContractFilesUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { files ->
                    val sortedFiles = files.sortedBy { it.displayOrder ?: Int.MAX_VALUE }
                    signContractFiles.value = sortedFiles

                    if (sortedFiles.size > 1) {
                        isMultiSigning.value = true
                        currentContractFileIndex.value = 0
                        currentRequestId.value = sortedFiles[0].signContractRequestId
                        loading.value = false
                        navigateToNextStep()
                    } else {
                        isMultiSigning.value = false
                        if (sortedFiles.isNotEmpty()) {
                            currentRequestId.value = sortedFiles[0].signContractRequestId
                        }
                        getSignContractSteps()
                    }
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
                        contractFileModelList.value =
                            sortContractFiles(res.contractVersionDetailModel)
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
            val templateIdRaw = EnrollSDK.contractTemplateId
            val parsedIds = parseContractTemplateIds(templateIdRaw)
            isMultiSigning.value = parsedIds.size > 1

            params.value = GenerateSignContractSessionTokenUsecaseParams(
                tenantId = EnrollSDK.tenantId,
                tenantSecret = EnrollSDK.tenantSecret,
                applicantId = EnrollSDK.applicantId,
                contractTemplateId = templateIdRaw,
                contractTemplateIds = if (parsedIds.size > 1) parsedIds else null,
                contractParams = EnrollSDK.contractParameters,
                signContractMode = EnrollSDK.signContractMode,
                signContractFileUri = EnrollSDK.signContractFileUri,
                signContractFileBytes = EnrollSDK.signContractFileBytes,
                contractFileName = EnrollSDK.contractFileName,
                signContractApproach = EnrollSDK.signContractApproach,
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

    private fun parseContractTemplateIds(rawIds: String): List<String> {
        return rawIds.split(",")
            .map { it.trim().trim('[', ']', '"', '\'') }
            .filter { it.isNotBlank() }
    }

    private fun navigateToNextStep() {
        mailValue.value = TextFieldValue()
        currentPhoneNumber.value = null
        navController!!.navigate(currentContractLowRiskFRAScreenContent)
    }

    fun getNextContract() {
        Log.d("currentStepIndex", currentStepIndex.value.toString())
        Log.d("contractFileModelList", contractFileModelList.value!!.size.toString())
        if (getContractText() == contractFileModelList.value!![contractFileModelList.value!!.size - 1].signContractTextEnum) {
            getCurrentContract.value = false
            showAllContracts.value = true
        } else {
            currentStepIndex.value++
            getCurrentContract.value = true
        }
    }

    fun getNextContractFile() {
        val files = signContractFiles.value ?: return
        val nextIndex = currentContractFileIndex.value + 1
        if (nextIndex >= files.size) {
            showAllContracts.value = true
        } else {
            currentContractFileIndex.value = nextIndex
            currentRequestId.value = files[nextIndex].signContractRequestId
            getCurrentContract.value = true
        }
    }

    fun getCurrentContractFileName(): String {
        val files = signContractFiles.value ?: return ""
        val index = currentContractFileIndex.value
        return files.getOrNull(index)?.fileName ?: ""
    }

    fun getContractProgress(): String {
        val files = signContractFiles.value ?: return ""
        return "${currentContractFileIndex.value + 1} / ${files.size}"
    }

    fun getContractText(): String {
        return contractFileModelList.value!![currentStepIndex.value].signContractTextEnum!!
    }

    private fun sortContractFiles(
        contractFiles: ArrayList<ContractFileModel>
    ): ArrayList<ContractFileModel> {
        return ArrayList(
            contractFiles.sortedWith(
                compareBy<ContractFileModel> { it.sectionOrder ?: Int.MAX_VALUE }
                    .thenBy { it.signContractTextEnum?.toIntOrNull() ?: Int.MAX_VALUE }
            )
        )
    }
}

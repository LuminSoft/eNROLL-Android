package com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model

import UpdateScanType
import android.graphics.Bitmap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import checkDeviceIdAuthUpdateScreenContent
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_navigation_update.multipleMailsUpdateScreenContent
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_navigation_update.multiplePhonesUpdateScreenContent
import com.luminsoft.enroll_sdk.features_update.update_location.update_location_navigation.updateLocationScreenContent
import com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_navigation.updateNationalIdPreScanScreen
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.ChooseStep
import com.luminsoft.enroll_sdk.main.main_presentation.common.MainViewModel
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_models.get_update_configurations.StepUpdateModel
import com.luminsoft.enroll_sdk.main_update.main_update_data.models.UpdateVerificationMethodResponse
import com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases.GenerateUpdateSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases.GenerateUpdateSessionTokenUsecaseParams
import com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases.GetUpdateAuthenticationMethodUsecase
import com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases.GetUpdateStepConfigurationsUsecaseParams
import com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases.UpdateStepIdParam
import com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases.UpdateStepsConfigurationsUsecase
import com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases.UpdateStepsInitRequestUsecase
import faceCaptureAuthUpdatePreScanScreenContent
import kotlinx.coroutines.flow.MutableStateFlow
import passwordAuthUpdateScreenContent
import securityQuestionAuthUpdateScreenContent
import testUpdateScreenContent

class UpdateViewModel(
    private val generateUpdateSessionToken: GenerateUpdateSessionTokenUsecase,
    private val updateStepConfigurationsUsecase: UpdateStepsConfigurationsUsecase,
    private val updateStepIntRequestUseCase: UpdateStepsInitRequestUsecase,
    private val updateAuthenticationMethodUsecase: GetUpdateAuthenticationMethodUsecase

) : ViewModel(),
    MainViewModel {
    override var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    override var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    override var token: MutableStateFlow<String?> = MutableStateFlow(null)
    var customerId: MutableStateFlow<String?> = MutableStateFlow(null)
    var updateStepModel: MutableStateFlow<StepUpdateModel?> = MutableStateFlow(null)
    var facePhotoPath: MutableStateFlow<String?> = MutableStateFlow(null)
    var errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var fullPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var mailValue: MutableStateFlow<TextFieldValue?> = MutableStateFlow(TextFieldValue())
    var phoneValue: MutableStateFlow<TextFieldValue?> = MutableStateFlow(TextFieldValue())
    var currentPhoneNumberCode: MutableStateFlow<String?> = MutableStateFlow("+20")
    var steps: MutableStateFlow<List<StepUpdateModel>?> = MutableStateFlow(null)
    var navController: NavController? = null
    var smileImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdFrontImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var passportImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdBackImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var scanType: MutableStateFlow<UpdateScanType?> = MutableStateFlow(null)
    var isNotFirstPhone: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var preScanLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isNotFirstMail: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var securityQuestions: MutableStateFlow<List<GetSecurityQuestionsResponseModel>?> =
        MutableStateFlow(null)
    var selectedSecurityQuestions: MutableStateFlow<ArrayList<GetSecurityQuestionsResponseModel>> =
        MutableStateFlow(arrayListOf())
    var securityQuestionsList: MutableStateFlow<ArrayList<GetSecurityQuestionsResponseModel>> =
        MutableStateFlow(arrayListOf())
    var isPassportAndMail: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isPassportAndMailFinal: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var chosenStep: MutableStateFlow<ChooseStep?> = MutableStateFlow(ChooseStep.NationalId)
    var selectedStep: MutableStateFlow<ChooseStep?> = MutableStateFlow(null)
    var updateStepId: MutableStateFlow<Int?> = MutableStateFlow(null)
    var updateAuthenticationStep: MutableStateFlow<UpdateVerificationMethodResponse?> =
        MutableStateFlow(null)
    var userMail: MutableStateFlow<String?> = MutableStateFlow(null)
    var userPhone: MutableStateFlow<String?> = MutableStateFlow(null)
    var mailId: MutableStateFlow<Int?> = MutableStateFlow(null)
    var phoneId: MutableStateFlow<Int?> = MutableStateFlow(null)

    override fun retry(navController: NavController) {
        TODO("Not yet implemented")
    }


    fun enableLoading() {
        loading.value = true
    }

    fun disableLoading() {
        loading.value = false
    }


    fun enablePreScanLoading() {
        preScanLoading.value = true
    }

    fun disablePreScanLoading() {
        preScanLoading.value = false
    }

    init {
        generateToken()
    }


    private fun generateToken() {
        loading.value = true
        ui {
            params.value = GenerateUpdateSessionTokenUsecaseParams(
                EnrollSDK.tenantId,
                EnrollSDK.tenantSecret,
                EnrollSDK.applicantId,
                EnrollSDK.updateSteps
            )

            val response: Either<SdkFailure, String> =
                generateUpdateSessionToken.call(params.value as GenerateUpdateSessionTokenUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        token.value = it1
                        RetroClient.setToken(it1)
                        params.value = GetUpdateStepConfigurationsUsecaseParams()
                        val responseData: Either<SdkFailure, List<StepUpdateModel>> =
                            updateStepConfigurationsUsecase.call(params.value as GetUpdateStepConfigurationsUsecaseParams)
                        responseData.fold({
                            failure.value = it
                            loading.value = false
                        }, { list ->
                            steps.value = list
                            loading.value = false
                        })

                    }
                })
        }
    }

    fun updateStepsInitRequestCall(updateStep: StepUpdateModel) {
        updateStepsInitRequest(updateStep)
    }

    private fun updateStepsInitRequest(updateStep: StepUpdateModel) {
        loading.value = true
        ui {
            params.value = UpdateStepIdParam(
                updateStepId = updateStep.updateStepId!!
            )

            val response: Either<SdkFailure, Null> =
                updateStepIntRequestUseCase
                    .call(params.value as UpdateStepIdParam)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    updateStepModel.value = updateStep
                    getUpdateAuthenticationStep(updateStep)

                })
        }
    }

    private fun getUpdateAuthenticationStep(updateStep: StepUpdateModel) {
//        loading.value = true
        ui {

            val response: Either<SdkFailure, UpdateVerificationMethodResponse> =
                updateAuthenticationMethodUsecase.call(updateStep.updateStepId!!)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    updateStepId.value = updateStep.updateStepId
                    updateAuthenticationStep.value = it
                    updateStepModel.value?.updateAuthStepId = it.authStepId
                    navigateToAuthStep(navController!!, it.authStepId!!)
                })
        }
    }


    private fun navigateToAuthStep(navController: NavController, stepId: Int) {
        val route = when (stepId) {
            1 -> passwordAuthUpdateScreenContent
            2 -> null   //TODO: email is blocked
            3 -> securityQuestionAuthUpdateScreenContent
            4 -> checkDeviceIdAuthUpdateScreenContent
            5 -> null   //TODO: phone is blocked
            6 -> faceCaptureAuthUpdatePreScanScreenContent
            else -> securityQuestionAuthUpdateScreenContent
        }
        route?.let {
            navController.navigate(it)
        }
    }

    fun navigateToUpdateAfterAuthStep() {
        //TODO: will navigate to update modules
        val route = when (updateStepId.value) {
            1 -> updateNationalIdPreScanScreen
            2 -> testUpdateScreenContent
            3 -> multiplePhonesUpdateScreenContent
            4 -> multipleMailsUpdateScreenContent
            5 -> testUpdateScreenContent
            6 -> updateLocationScreenContent
            7 -> testUpdateScreenContent
            8 -> testUpdateScreenContent
            else -> null
        }
        route?.let {
            navController?.navigate(it)
        }
    }


    fun convertStepUpdateIdToTitle(): String {
        val title = when (updateStepId.value) {
            1 -> "Update NationalID"
            2 -> "Update Passport"
            3 -> "Update Phone"
            4 -> "Update Email"
            5 -> "Update Device"
            6 -> "Update Location"
            7 -> "Update Security Questions"
            8 -> "Update Password"
            else -> "Update"
        }
        return title

    }


    fun removeCurrentStep(id: Int): Boolean {
        // Check if 'steps' is not null
        if (steps.value != null) {
            // Store the initial size of the 'steps' list
            val stepsSize = steps.value!!.size

            // Create a mutable list from 'steps', remove the item with the given ID, and convert back to an immutable list
            steps.value = steps.value!!.toMutableList().apply {
                removeIf { x -> x.updateStepId == id }
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
        navController!!.navigate(steps.value!!.first().stepUpdateNameNavigator())
    }

    private fun navigateBackToUpdate() {
        navController!!.navigate(steps.value!!.first().stepUpdateNameNavigator())
    }

    fun navigateToTheSameStep() {
        navController!!.navigate(steps.value!!.first().stepUpdateNameNavigator())
    }

    fun updateMailId(id: Int) {
        mailId.value = id
    }

    fun updatePhoneId(id: Int) {
        phoneId.value = id
    }
}
package com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.models.BuildInfo
import com.luminsoft.enroll_sdk.core.models.EnrollSuccessModel
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_applicatnt_id.GetApplicantIdResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_current_step.GetCurrentStepResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.ChooseStep
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestResponse
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecaseParams
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GetApplicantIdUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GetCurrentStepUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GetOnboardingStepConfigurationsUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GetOnboardingStepConfigurationsUsecaseParams
import com.luminsoft.enroll_sdk.main.main_domain.usecases.InitializeRequestUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.InitializeRequestUsecaseParams
import com.luminsoft.enroll_sdk.main.main_navigation.onBoardingScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.common.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class OnBoardingViewModel(
    private val generateOnboardingSessionToken: GenerateOnboardingSessionTokenUsecase,
    private val getOnboardingStepConfigurationsUsecase: GetOnboardingStepConfigurationsUsecase,
    private val initializeRequestUsecase: InitializeRequestUsecase,
    private val getApplicantIdUsecase: GetApplicantIdUsecase,
    private val getCurrentStepUsecase: GetCurrentStepUsecase,
    private val context: Context

) : ViewModel(),
    MainViewModel {
    override var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    override var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    override var token: MutableStateFlow<String?> = MutableStateFlow(null)
    var customerId: MutableStateFlow<String?> = MutableStateFlow(null)
    var documentId: MutableStateFlow<String?> = MutableStateFlow(null)

    val existingSteps: MutableState<List<Int>?> = mutableStateOf(null)
    var requestId: MutableStateFlow<String?> = MutableStateFlow(null)
    var applicantId: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentStepId: MutableStateFlow<Int?> = MutableStateFlow(null)
    var requestCallBackSent: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var facePhotoPath: MutableStateFlow<String?> = MutableStateFlow(null)
    var errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var mailValue: MutableStateFlow<TextFieldValue?> = MutableStateFlow(TextFieldValue())
    var currentPhoneNumberCode: MutableStateFlow<String?> = MutableStateFlow("+20")
    var steps: MutableStateFlow<List<StepModel>?> = MutableStateFlow(null)
    var navController: NavController? = null
    var activity: Activity? = null
    var smileImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdFrontImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var passportImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdBackImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var scanType: MutableStateFlow<ScanType?> = MutableStateFlow(null)
    var isNotFirstPhone: MutableStateFlow<Boolean> = MutableStateFlow(false)
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
    
    // Exit step tracking - indicates SDK closed at a specific step (not fully completed)
    var exitStepReached: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var exitStepName: MutableStateFlow<String?> = MutableStateFlow(null)
    
    // Completion dialog state - shows success dialog when step/flow is completed
    var showCompletionDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var completionDialogMessage: MutableStateFlow<String> = MutableStateFlow("")
    var isCompletionExitStep: MutableStateFlow<Boolean> = MutableStateFlow(false)


    init {
        generateToken()
    }

    override fun retry(navController: NavController) {
        generateToken()
    }


    private fun extractRegistrationStepIds(steps: List<StepModel>) {
        existingSteps.value = steps.map { it.registrationStepId!! }
    }

    fun initRequest() {
        loading.value = true
        failure.value = null
        ui {
            if (EnrollSDK.requestId.isNotEmpty())
                getCurrentStep()
            val deviceId = DeviceIdentifier.getDeviceId(context)
            val manufacturer: String = Build.MANUFACTURER
            val deviceModel: String = Build.MODEL

            params.value = InitializeRequestUsecaseParams(
                deviceId,
                manufacturer,
                deviceModel,
                "Android",
                BuildInfo.SDK_VERSION
            )
            val response: Either<SdkFailure, InitializeRequestResponse> =
                initializeRequestUsecase.call(params.value as InitializeRequestUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    loading.value = false
                    requestId.value = it.requestId
                    if (EnrollSDK.skipTutorial) {
                        EnrollSDK.enrollCallback?.getRequestId(requestId.value!!)
                        changeRequestIdSentValue()
                    }
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


    private fun generateToken(retryWithoutRequestId: Boolean = false) {
        loading.value = true
        ui {
            val uuid: String = UUID.randomUUID().toString()
            // If retrying, use empty requestId to start fresh
            val requestIdToUse = if (retryWithoutRequestId) "" else EnrollSDK.requestId
            
            params.value = GenerateOnboardingSessionTokenUsecaseParams(
                EnrollSDK.tenantId,
                EnrollSDK.tenantSecret,
                uuid,
                EnrollSDK.correlationId,
                requestIdToUse
            )

            val response: Either<SdkFailure, String> =
                generateOnboardingSessionToken.call(params.value as GenerateOnboardingSessionTokenUsecaseParams)

            response.fold(
                {
                    // Only retry without requestId if the request was REJECTED
                    // For complete/invalid requests, show the validation error instead
                    val isRejectedRequest = isRequestRejected(it)
                    
                    if (!retryWithoutRequestId && EnrollSDK.requestId.isNotEmpty() && isRejectedRequest) {
                        // Rejected request: restart from beginning
                        EnrollSDK.requestId = "" // Clear rejected requestId
                        generateToken(retryWithoutRequestId = true)
                    } else {
                        // Complete/invalid/other errors: show validation message
                        failure.value = it
                        loading.value = false
                    }
                },
                { s ->
                    s.let { it1 ->
                        token.value = it1
                        RetroClient.setToken(it1)
                        params.value = GetOnboardingStepConfigurationsUsecaseParams()
                        val responseData: Either<SdkFailure, List<StepModel>> =
                            getOnboardingStepConfigurationsUsecase.call(params.value as GetOnboardingStepConfigurationsUsecaseParams)
                        responseData.fold({
                            failure.value = it
                            loading.value = false
                        }, { list ->
                            steps.value = list
                            loading.value = false
                            extractRegistrationStepIds(list)

                            if (EnrollSDK.skipTutorial) {
                                initRequest()
                            } else {
                                navController!!.navigate(onBoardingScreenContent)
                            }

                        })

                    }
                })
        }
    }

    suspend fun getApplicantId(): Either<SdkFailure, GetApplicantIdResponse> {
        loading.value = true

        val response: Either<SdkFailure, GetApplicantIdResponse> =
            getApplicantIdUsecase.call(null)


        response.fold(
            { failure ->
                this.failure.value = failure
                loading.value = false
            },
            { success ->
                applicantId.value = success.applicantId
                loading.value = false
            }
        )

        return response  // Return the response
    }

    private suspend fun getCurrentStep(): Either<SdkFailure, GetCurrentStepResponse> {
        loading.value = true

        val response: Either<SdkFailure, GetCurrentStepResponse> =
            getCurrentStepUsecase.call(null)


        response.fold(
            { failure ->
                this.failure.value = failure
                loading.value = false
            },
            { success ->
                currentStepId.value = success.currentStepId
                removeStepsUntilCurrentStep()
            }
        )

        return response  // Return the response
    }


    /**
     * Removes the current step and handles navigation or completion automatically.
     * 
     * This method handles everything internally - no UI code needed in feature screens:
     * - If more steps remain: navigates to next step automatically
     * - If exit step reached: triggers success callback and finishes activity
     * - If all steps completed: fetches applicantId, triggers callback, finishes activity
     * 
     * @param id The step ID that was just completed
     */
    fun removeCurrentStep(id: Int) {
        try {
            // Check if this step is the configured exit step
            val exitStep = EnrollSDK.exitStep
            if (exitStep != null && exitStep.getStepId() == id) {
                exitStepReached.value = true
                exitStepName.value = exitStep.name
                // Remove the current step from the list
                if (steps.value != null) {
                    steps.value = steps.value!!.toMutableList().apply {
                        removeIf { x -> x.registrationStepId == id }
                    }.toList()
                }
                // Handle completion automatically (exit step - skip getApplicantId)
                handleCompletionAutomatically(isExitStep = true)
                return
            }
            
            if (steps.value != null) {
                val stepsSize = steps.value!!.size
                steps.value = steps.value!!.toMutableList().apply {
                    removeIf { x -> x.registrationStepId == id }
                }.toList()
                val newStepsSize = steps.value!!.size
                if (stepsSize != newStepsSize) {
                    if (steps.value!!.isNotEmpty()) {
                        navigateToNextStep()
                    } else {
                        // All steps completed - handle completion automatically
                        handleCompletionAutomatically(isExitStep = false)
                    }
                }
            }
        } catch (e: Exception) {
            // Log error if needed
        }
    }
    
    /**
     * Handles SDK completion - shows success dialog before finishing.
     * User must confirm dialog to trigger callback and finish activity.
     * 
     * @param isExitStep True if exiting at a defined step, false if full completion
     */
    private fun handleCompletionAutomatically(isExitStep: Boolean) {
        ui {
            // For full completion, fetch applicant ID first
            if (!isExitStep) {
                getApplicantId()
            }
            
            // Set dialog message based on completion type
            completionDialogMessage.value = if (isExitStep) {
                context.getString(R.string.stepCompletedSuccessfully)
            } else {
                context.getString(R.string.successfulRegistration)
            }
            
            // Store exit step flag for callback
            isCompletionExitStep.value = isExitStep
            
            // Show the completion dialog
            showCompletionDialog.value = true
        }
    }
    
    /**
     * Called when user confirms the completion dialog.
     * Triggers success callback and finishes the activity.
     */
    fun onCompletionDialogConfirmed() {
        // Trigger success callback
        EnrollSDK.enrollCallback?.success(
            EnrollSuccessModel(
                enrollMessage = completionDialogMessage.value,
                documentId = documentId.value,
                applicantId = applicantId.value,
                requestId = requestId.value,
                exitStepCompleted = isCompletionExitStep.value,
                completedStepName = exitStepName.value
            )
        )
        
        // Clear requestId after successful completion (not exit step) to prevent
        // reusing a completed requestId on next SDK launch
        if (!isCompletionExitStep.value) {
            EnrollSDK.requestId = ""
        }
        
        // Hide dialog and finish activity
        showCompletionDialog.value = false
        activity?.finish()
    }

    private fun navigateToNextStep() {
        if (navController == null || steps.value.isNullOrEmpty()) return
        mailValue.value = TextFieldValue()
        currentPhoneNumber.value = null
        navController!!.navigate(steps.value!!.first().stepNameNavigator())
    }

    fun navigateToTheSameStep() {
        navController!!.navigate(steps.value!!.first().stepNameNavigator())
    }

    fun changeRequestIdSentValue() {
        requestCallBackSent.value = true
    }


    private fun removeStepsUntilCurrentStep() {
        // Check if the steps list is not null and contains at least one step
        steps.value?.let { stepList ->
            // Find the index of the step with matching currentStepId
            val currentStepIndex =
                stepList.indexOfFirst { it.registrationStepId == currentStepId.value }

            // If a matching step is found
            if (currentStepIndex != -1) {
                // Remove all steps up to and including the found step
                steps.value = stepList.subList(currentStepIndex, stepList.size)
            }
        }
    }
    
    /**
     * Checks if the failure indicates a REJECTED request status.
     * Only rejected requests should trigger a restart from beginning.
     * Complete and invalid requests should show validation errors instead.
     * 
     * @param failure The SDK failure to check
     * @return true if the request was rejected, false for complete/invalid/other errors
     */
    private fun isRequestRejected(failure: SdkFailure): Boolean {
        val message = failure.message.lowercase()
        // Check for rejected status keywords
        // Rejected requests need to restart all steps
        return message.contains("reject") || message.contains("rejected")
    }
}
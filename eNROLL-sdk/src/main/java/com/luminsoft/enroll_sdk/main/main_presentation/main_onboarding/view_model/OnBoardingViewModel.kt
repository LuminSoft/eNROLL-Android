package com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model

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
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_applicatnt_id.GetApplicantIdResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.ChooseStep
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestResponse
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecaseParams
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GetApplicantIdUsecase
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

    //    var userNationalId: MutableStateFlow<String?> = MutableStateFlow(null)
//    var userPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
//    var userMail: MutableStateFlow<String?> = MutableStateFlow(null)
    val existingSteps: MutableState<List<Int>?> = mutableStateOf(null)
    var requestId: MutableStateFlow<String?> = MutableStateFlow(null)
    var applicantId: MutableStateFlow<String?> = MutableStateFlow(null)
    var requestCallBackSent: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var facePhotoPath: MutableStateFlow<String?> = MutableStateFlow(null)
    var errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var mailValue: MutableStateFlow<TextFieldValue?> = MutableStateFlow(TextFieldValue())
    var currentPhoneNumberCode: MutableStateFlow<String?> = MutableStateFlow("+20")
    var steps: MutableStateFlow<List<StepModel>?> = MutableStateFlow(null)
    var navController: NavController? = null
    var smileImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdFrontImage: MutableStateFlow<String?> = MutableStateFlow(null)
    var passportImage: MutableStateFlow<String?> = MutableStateFlow(null)
    var nationalIdBackImage: MutableStateFlow<String?> = MutableStateFlow(null)
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


    init {
//        generateToken()
        getOnboardingStepConfigurations()
    }

    override fun retry(navController: NavController) {
        TODO("Not yet implemented")
    }


    private fun extractRegistrationStepIds(steps: List<StepModel>) {
        existingSteps.value = steps.map { it.registrationStepId!! }
    }

    fun initRequest() {
        loading.value = true
        ui {

            val deviceId = DeviceIdentifier.getDeviceId(context)
            val manufacturer: String = Build.MANUFACTURER
            val deviceModel: String = Build.MODEL

            params.value = InitializeRequestUsecaseParams(
                deviceId,
                manufacturer,
                deviceModel
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


    private fun getOnboardingStepConfigurations() {
        val generatedToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjE3MzI2Mjc4Mjg3NjYiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoic3RyaW5nIiwianRpIjoiYzJmYTQ1YTEtN2MzYy00NDIyLTgzZTYtYzkyMjhjOWNjYzM3IiwiT3JnYW5pemF0aW9uSWQiOiJwRkFZclhSUmZOSFpSeHB4eGJVK1NnPT0iLCJPcmdSZWdTdGVwc1ZlcnNpb24iOiJRRWFxNVc5dWVvWG5yRzIwR2dVMzJBPT0iLCJSZXF1ZXN0VHlwZSI6Ik9uYm9hcmRpbmdSZXF1ZXN0IiwiQ29ycmVsYXRpb25JZCI6InpZTHhLSDhhTFQrV01mUUhPZi9SWGc9PSIsImV4cCI6MTczMjYyODcyOCwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo1MDAwIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo0NTAwIn0.ZZaoIubmQaT9clW4WYdq7GnhWeVzg-Lh_qk4NZ-o-gg"
        loading.value = true
        token.value = generatedToken
        RetroClient.setToken(generatedToken)
        ui {
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
                println("steps length = ${steps.value!!.size}")

                if (EnrollSDK.skipTutorial) {
                    initRequest()
                } else {
                    navController!!.navigate(onBoardingScreenContent)
                }

            })
        }


    }


    private fun generateToken() {
        loading.value = true
        ui {
            val uuid: String = UUID.randomUUID().toString()
            params.value = GenerateOnboardingSessionTokenUsecaseParams(
                EnrollSDK.tenantId,
                EnrollSDK.tenantSecret,
                uuid,
                EnrollSDK.correlationId
            )

            val response: Either<SdkFailure, String> =
                generateOnboardingSessionToken.call(params.value as GenerateOnboardingSessionTokenUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
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


    fun removeCurrentStep(id: Int): Boolean {
        if (steps.value != null) {
            val stepsSize = steps.value!!.size
            steps.value = steps.value!!.toMutableList().apply {
                removeIf { x -> x.registrationStepId == id }
            }.toList()
            val newStepsSize = steps.value!!.size
            if (stepsSize != newStepsSize) {
                return if (steps.value!!.isNotEmpty()) {
                    navigateToNextStep()
                    false
                } else
                    true
            }
        }
        return false
    }

    private fun navigateToNextStep() {
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
}

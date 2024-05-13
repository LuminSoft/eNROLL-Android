package com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation.phoneNumbersOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation.validateOtpPhoneNumberScreenContent
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_navigation.securityQuestionsOnBoardingScreenContent
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecaseParams
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
    private val initializeRequestUsecase: InitializeRequestUsecase
) : ViewModel(),
    MainViewModel {
    override var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    override var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    override var token: MutableStateFlow<String?> = MutableStateFlow(null)
    var customerId: MutableStateFlow<String?> = MutableStateFlow(null)
    var facePhotoPath: MutableStateFlow<String?> = MutableStateFlow(null)
    var errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var mailValue: MutableStateFlow<TextFieldValue?> = MutableStateFlow(TextFieldValue())
    var currentPhoneNumberCode: MutableStateFlow<String?> = MutableStateFlow("+20")
    var steps: MutableStateFlow<List<StepModel>?> = MutableStateFlow(null)
    var navController: NavController? = null
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

    override fun retry(navController: NavController) {
        TODO("Not yet implemented")
    }

    fun initRequest() {
        loading.value = true
        ui {
            val uuid: String = UUID.randomUUID().toString()
            val manufacturer: String = Build.MANUFACTURER
            val deviceModel: String = Build.MODEL

            params.value = InitializeRequestUsecaseParams(
                uuid,
                manufacturer,
                deviceModel
            )
            val response: Either<SdkFailure, Null> =
                initializeRequestUsecase.call(params.value as InitializeRequestUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    loading.value = false
                    navController!!.navigate(securityQuestionsOnBoardingScreenContent)
//                    navigateToNextStep()
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
//            delay(2000)
            val udid: String = UUID.randomUUID().toString()
            params.value = GenerateOnboardingSessionTokenUsecaseParams(
                EnrollSDK.tenantId,
                EnrollSDK.tenantSecret,
                udid
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
                        val response: Either<SdkFailure, List<StepModel>> =
                            getOnboardingStepConfigurationsUsecase.call(params.value as GetOnboardingStepConfigurationsUsecaseParams)
                        response.fold({
                            failure.value = it
                            loading.value = false
                        }, { list ->
                            steps.value = list
                            loading.value = false
                            navController!!.navigate(onBoardingScreenContent)
                        })

                    }
                })
        }
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
}
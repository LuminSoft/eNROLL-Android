package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model


import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
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
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.ChooseStep
import com.luminsoft.enroll_sdk.main.main_presentation.common.MainViewModel
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.get_forget_configurations.StepForgetModel
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.ForgetStepsConfigurationsUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.GenerateForgetSessionForStepTokenUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.GenerateForgetSessionTokenForStepUsecaseParams
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.GenerateForgetSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.GenerateForgetSessionTokenUsecaseParams
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.GetForgetStepConfigurationsUsecaseParams
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.InitializeForgetRequestUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.VerifyPasswordUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.VerifyPasswordUsecaseParams
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_navigation.verifyPasswordScreenContent
import forgetLocationScreenContent
import kotlinx.coroutines.flow.MutableStateFlow
import passwordAuthUpdateScreenContent

class ForgetViewModel(
    private val generateForgetSessionToken: GenerateForgetSessionTokenUsecase,
    private val forgetStepConfigurationsUsecase: ForgetStepsConfigurationsUsecase,
    private val generateForgetSessionForStepTokenUsecase: GenerateForgetSessionForStepTokenUsecase,
    private val initializeForgetRequestUsecase: InitializeForgetRequestUsecase,
    private val verifyPasswordUsecase: VerifyPasswordUsecase,

    ) : ViewModel(),
    MainViewModel {
    override var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    override var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    override var token: MutableStateFlow<String?> = MutableStateFlow(null)
     var forgetStepToken: MutableStateFlow<String?> = MutableStateFlow(null)
     var originalToken: MutableStateFlow<String?> = MutableStateFlow(null)
    var customerId: MutableStateFlow<String?> = MutableStateFlow(null)
    var forgetStepModel: MutableStateFlow<StepForgetModel?> = MutableStateFlow(null)
    var facePhotoPath: MutableStateFlow<String?> = MutableStateFlow(null)
    var errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var fullPhoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    var mailValue: MutableStateFlow<TextFieldValue?> = MutableStateFlow(TextFieldValue())
    var nationalIdValue: MutableStateFlow<TextFieldValue?> = MutableStateFlow(TextFieldValue())
    var nationalIdError: MutableState<String?> = mutableStateOf(null)
    var passwordValue: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue())
    var passwordError: MutableState<String?> = mutableStateOf(null)
    var phoneValue: MutableStateFlow<TextFieldValue?> = MutableStateFlow(TextFieldValue())
    var currentPhoneNumberCode: MutableStateFlow<String?> = MutableStateFlow("+20")
    var steps: MutableStateFlow<List<StepForgetModel>?> = MutableStateFlow(null)
    var navController: NavController? = null
    var smileImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdFrontImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var passportImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdBackImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var isNotFirstPhone: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var preScanLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isNotFirstMail: MutableStateFlow<Boolean> = MutableStateFlow(false)


    var isPassportAndMail: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isPassportAndMailFinal: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var chosenStep: MutableStateFlow<ChooseStep?> = MutableStateFlow(ChooseStep.NationalId)
    var selectedStep: MutableStateFlow<ChooseStep?> = MutableStateFlow(null)
    var forgetStepId: MutableStateFlow<Int?> = MutableStateFlow(null)

    var userMail: MutableStateFlow<String?> = MutableStateFlow(null)
    var userPhone: MutableStateFlow<String?> = MutableStateFlow(null)
    var mailId: MutableStateFlow<Int?> = MutableStateFlow(null)
    var phoneId: MutableStateFlow<Int?> = MutableStateFlow(null)
    var verifiedPhones: MutableStateFlow<List<GetVerifiedPhonesResponseModel>?> =
        MutableStateFlow(null)
    var verifiedMails: MutableStateFlow<List<GetVerifiedMailsResponseModel>?> =
        MutableStateFlow(null)

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
            params.value = GenerateForgetSessionTokenUsecaseParams(
                EnrollSDK.tenantId,
                EnrollSDK.tenantSecret,
            )

            val response: Either<SdkFailure, String> =
                generateForgetSessionToken.call(params.value as GenerateForgetSessionTokenUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        token.value = it1
                        RetroClient.setToken(it1)

                        params.value = GetForgetStepConfigurationsUsecaseParams()
                        val responseData: Either<SdkFailure, List<StepForgetModel>> =
                            forgetStepConfigurationsUsecase.call(params.value as GetForgetStepConfigurationsUsecaseParams)
                        responseData.fold({
                            failure.value = it
                            loading.value = false
                        }, { list ->
                            val mutableList = list.toMutableList()
                            steps.value = mutableList
                            loading.value = false
                        })
                    }
                })
        }
    }


     fun generateForgetTokenForStep() {
        loading.value = true
        ui {
            params.value =
                GenerateForgetSessionTokenForStepUsecaseParams(
                    step = forgetStepId.value!!,
                    nationalIdOrPassportNumber =nationalIdValue.value?.text.toString(),
                )

            val response: Either<SdkFailure, String> =
                generateForgetSessionForStepTokenUsecase.call(params.value as GenerateForgetSessionTokenForStepUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    // here we saved the original token to restore it later
                    originalToken.value = token.value
                    token.value = it
                    RetroClient.setToken(it)

                    params.value = forgetStepId.value
                    val responseData: Either<SdkFailure, Null> =
                        initializeForgetRequestUsecase.call(params.value as Int)
                    responseData.fold({
                        failure.value = it
                        loading.value = false
                    }, {
                        nationalIdValue.value = TextFieldValue("")
                        loading.value = false
                        navigateToAuthStep(navController!!, forgetStepId.value!!)
                    })
                })
        }
    }

    fun verifyPassword() {
        loading.value = true
        ui {
            params.value =
                VerifyPasswordUsecaseParams(
                    password = passwordValue.value.text,
                    updateStepId = forgetStepId.value!!,
                )
            val response: Either<SdkFailure, Null> =
                verifyPasswordUsecase.call(params.value as VerifyPasswordUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    passwordValue.value = TextFieldValue("")
                    loading.value = false
                    navigateToForgetAfterAuthStep()
                })
        }
    }

    private fun navigateToAuthStep(navController: NavController, stepId: Int) {
        val route = when (stepId) {
            1 -> passwordAuthUpdateScreenContent //TODO National ID
            2 -> passwordAuthUpdateScreenContent //TODO Passport
            3 -> passwordAuthUpdateScreenContent //TODO Phone
            4 -> passwordAuthUpdateScreenContent //TODO Email
            5 -> passwordAuthUpdateScreenContent //TODO Device
            6 -> verifyPasswordScreenContent
            7 -> passwordAuthUpdateScreenContent //TODO Security Questions
            8 -> passwordAuthUpdateScreenContent //TODO Password
            9 -> passwordAuthUpdateScreenContent //TODO Check AML
            10 -> passwordAuthUpdateScreenContent //TODO Electronic Signature
            else -> passwordAuthUpdateScreenContent //TODO
        }
        route.let {
            navController.navigate(it)
        }
    }

    private fun navigateToForgetAfterAuthStep() {
        val route = when (forgetStepId.value) {
            1 -> passwordAuthUpdateScreenContent //TODO
            2 -> passwordAuthUpdateScreenContent //TODO
            3 -> passwordAuthUpdateScreenContent //TODO
            4 -> passwordAuthUpdateScreenContent //TODO
            5 -> passwordAuthUpdateScreenContent //TODO
            6 -> forgetLocationScreenContent
            7 -> passwordAuthUpdateScreenContent //TODO
            8 -> passwordAuthUpdateScreenContent //TODO
            else -> null
        }
        route?.let {
            navController?.navigate(it)
        }
    }


    fun convertStepForgetIdToTitle(): String {
        val title = when (forgetStepId.value) {
            1 -> "Forget NationalID"
            2 -> "Forget Passport"
            3 -> "Forget Phone"
            4 -> "Forget Email"
            5 -> "Forget Device"
            6 -> "Forget Location"
            7 -> "Forget Security Questions"
            8 -> "Forget Password"
            else -> "Forget"
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
                removeIf { x -> x.forgetStepId == id }
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
        navController!!.navigate(steps.value!!.first().stepForgetNameNavigator())
    }

    private fun navigateBackToForget() {
        navController!!.navigate(steps.value!!.first().stepForgetNameNavigator())
    }

    fun navigateToTheSameStep() {
        navController!!.navigate(steps.value!!.first().stepForgetNameNavigator())
    }

    fun forgetMailId(id: Int) {
        mailId.value = id
    }

    fun forgetPhoneId(id: Int) {
        phoneId.value = id
    }
}
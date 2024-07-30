package com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model

import androidx.lifecycle.ViewModel
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.main.main_data.main_models.OnBoardingPage
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.RegistrationStepSetting
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import kotlinx.coroutines.flow.MutableStateFlow

class TutorialViewModel(val steps: MutableStateFlow<List<StepModel>?>) : ViewModel() {
    var pages: MutableStateFlow<List<OnBoardingPage>?> = MutableStateFlow(null)

    init {
        init()
    }

    fun init() {
        ui {
            val pages: MutableList<OnBoardingPage> = arrayListOf()
            steps.value?.forEach {
                if (it.parseEkycStepType() != EkycStepType.PersonalConfirmation) {
                    pages.add(getOnBoardingPage(it.parseEkycStepType()))
                } else {
                    for (i in it.organizationRegStepSettings.indices) {
                        when (it.organizationRegStepSettings[i].parseRegistrationStepSetting()) {
                            RegistrationStepSetting.nationalIdOnly -> {
                                pages.add(OnBoardingPage.NationalIDOnlyPage)
                            }

                            RegistrationStepSetting.passportOnly -> {
                                pages.add(OnBoardingPage.PassportOnlyPage)
                            }

                            RegistrationStepSetting.nationalIdOrPassport -> {
                                pages.add(OnBoardingPage.NationalIdOrPassportPage)

                            }

                            RegistrationStepSetting.nationalIdAndPassport -> {
                                pages.add(OnBoardingPage.NationalIDOnlyPage)
                                pages.add(OnBoardingPage.PassportOnlyPage)
                            }

                            else -> {
                                continue
                            }
                        }
                    }
                }

            }
            this.pages.value = pages
        }
    }

    private fun getOnBoardingPage(ekycStepType: EkycStepType): OnBoardingPage {
        return when (ekycStepType) {
            EkycStepType.PersonalConfirmation -> OnBoardingPage.NationalIDOnlyPage
            EkycStepType.SmileLiveness -> OnBoardingPage.SmileLivenessPage
            EkycStepType.PhoneOtp -> OnBoardingPage.PhoneOtpPage
            EkycStepType.EmailOtp -> OnBoardingPage.EmailOtpPage
            EkycStepType.DeviceLocation -> OnBoardingPage.DeviceLocationPage
            EkycStepType.SecurityQuestions -> OnBoardingPage.SecurityQuestionsPage
            EkycStepType.SettingPassword -> OnBoardingPage.SettingPasswordPage
            EkycStepType.AmlCheck -> OnBoardingPage.CheckingAmlPage
            else -> {
                OnBoardingPage.NationalIDOnlyPage
            }
        }
    }

}
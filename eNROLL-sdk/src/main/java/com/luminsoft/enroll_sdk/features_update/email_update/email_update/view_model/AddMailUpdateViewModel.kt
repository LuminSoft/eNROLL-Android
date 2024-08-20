package com.luminsoft.enroll_sdk.features_update.email_update.email_update.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_add_new_update.MailUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.SendOtpUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.UpdateMailAddUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.UpdateMailAddUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class AddMailUpdateViewModel(
    private val updateMailAddUseCase: UpdateMailAddUpdateUseCase,
    private val sendOtpUpdateUseCase: SendOtpUpdateUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var mailSentSuccessfully: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isClicked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var mailId: MutableStateFlow<Int?> = MutableStateFlow(null)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


    fun addMailCallApi(mail: String) {
        addMailCall(mail)
    }

    private fun addMailCall(mail: String) {
        loading.value = true
        ui {
            params.value =
                UpdateMailAddUseCaseParams(
                    email = mail
                )
            val response: Either<SdkFailure, MailUpdateAddNewResponseModel> =
                updateMailAddUseCase.call(params.value as UpdateMailAddUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    mailId.value = it.id
                    sendOtpCall(it.id!!)

                })
        }
    }

    private fun sendOtpCall(id: Int) {
        loading.value = true
        ui {
            val response: Either<SdkFailure, Null> =
                sendOtpUpdateUseCase.call(id)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
//                    loading.value = false
                    mailSentSuccessfully.value = true

                })
        }


    }

}
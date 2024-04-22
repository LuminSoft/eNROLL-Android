package com.luminsoft.enroll_sdk.features.email.email_onboarding.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.ApproveMailsUseCase
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MakeDefaultMailUseCase
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MakeDefaultMailUseCaseParams
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MultipleMailUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class MultipleMailsViewModel(
    private val multipleMailUseCase: MultipleMailUseCase,
    private val approveMailsUseCase: ApproveMailsUseCase,
    private val makeDefaultMailUseCase: MakeDefaultMailUseCase,
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var mailsApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var verifiedMails: MutableStateFlow<List<GetVerifiedMailsResponseModel>?> =
        MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


    fun callApproveMails() {
        approveMails()
    }

    fun callMakeDefaultMail(mail: String) {
        makeDefaultMail(mail)
    }

    init {
        getVerifiedMails()
    }

    private fun getVerifiedMails() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, List<GetVerifiedMailsResponseModel>> =
                multipleMailUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let {
                        verifiedMails.value = s
                    }
                    loading.value = false
                })
        }


    }

    private fun approveMails() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, Null> =
                approveMailsUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    mailsApproved.value = true
//                    loading.value = false
                })
        }


    }

    private fun makeDefaultMail(mail: String) {
        loading.value = true
        ui {
            params.value =
                MakeDefaultMailUseCaseParams(
                    email = mail
                )
            val response: Either<SdkFailure, Null> =
                makeDefaultMailUseCase.call(params.value as MakeDefaultMailUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    getVerifiedMails()
                })
        }


    }

}
package com.luminsoft.enroll_sdk.features_update.email_update.email_update.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MakeDefaultMailUseCaseParams
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.DeleteMailUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.GetApplicantEmailsUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.MakeDefaultMailUpdateUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class MultipleMailsUpdateViewModel(
    private val multipleMailUseCase: GetApplicantEmailsUseCase,
    private val deleteMailUpdateUseCase: DeleteMailUpdateUseCase,
    private val makeDefaultMailUpdateUseCase: MakeDefaultMailUpdateUseCase,
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var mailsUpdated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isDeleteMailClicked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var mailToDelete: MutableStateFlow<String?> = MutableStateFlow(null)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var verifiedMails: MutableStateFlow<List<GetVerifiedMailsResponseModel>?> =
        MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


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


    fun callMakeDefaultMail(mail: String) {
        makeDefaultMail(mail)
    }

    private fun makeDefaultMail(mail: String) {
        loading.value = true
        ui {
            params.value =
                MakeDefaultMailUseCaseParams(
                    email = mail
                )
            val response: Either<SdkFailure, Null> =
                makeDefaultMailUpdateUseCase.call(params.value as MakeDefaultMailUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    mailsUpdated.value = true
//                    getVerifiedMails()
                })
        }
    }

    fun callDeleteMail(mail: String) {
        deleteMail(mail)
    }

    private fun deleteMail(mail: String) {
        loading.value = true
        ui {
            params.value =
                MakeDefaultMailUseCaseParams(
                    email = mail
                )
            val response: Either<SdkFailure, Null> =
                deleteMailUpdateUseCase.call(params.value as MakeDefaultMailUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    mailsUpdated.value = true
//                    getVerifiedMails()
                })
        }
    }

}
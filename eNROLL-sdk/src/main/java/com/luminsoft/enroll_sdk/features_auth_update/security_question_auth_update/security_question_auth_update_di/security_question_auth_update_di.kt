import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth_update.security_question_auth_update.security_question_auth_update_domain.usecases.GetSecurityQuestionAuthUpdateUseCase
import com.luminsoft.enroll_sdk.features_auth_update.security_question_auth_update.security_question_auth_update_domain.usecases.ValidateSecurityQuestionAuthUpdateUseCase
import com.luminsoft.enroll_sdk.features_auth_update.security_question_auth_update.security_questions_auth_update_data.security_question_auth_update_api.SecurityQuestionAuthUpdateApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val securityQuestionAuthUpdateModule = module {
    single {
        GetSecurityQuestionAuthUpdateUseCase(get())
    }
    single {
        ValidateSecurityQuestionAuthUpdateUseCase(get())
    }
    single<SecurityQuestionAuthUpdateRemoteDataSource> {
        SecurityQuestionAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<SecurityQuestionAuthUpdateRepository> {
        SecurityQuestionAuthUpdateRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(SecurityQuestionAuthUpdateApi::class.java)
    }
    viewModel {
        SecurityQuestionAuthUpdateViewModel(get(), get())
    }

}
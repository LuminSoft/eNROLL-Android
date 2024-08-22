
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_question_auth_domain.usecases.GetSecurityQuestionAuthUseCase
import com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_question_auth_domain.usecases.ValidateSecurityQuestionUseCase
import com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_questions__auth_data.security_question_auth_api.SecurityQuestionAuthApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val securityQuestionAuthModule = module {
    single {
        GetSecurityQuestionAuthUseCase(get())
    }
    single {
        ValidateSecurityQuestionUseCase(get())
    }
    single<SecurityQuestionAuthRemoteDataSource> {
        SecurityQuestionAuthRemoteDataSourceImpl(get(), get())
    }
    single<SecurityQuestionAuthRepository> {
        SecurityQuestionAuthRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(SecurityQuestionAuthApi::class.java)
    }
    viewModel {
        SecurityQuestionAuthViewModel(get(), get())
    }

}
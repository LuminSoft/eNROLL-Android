
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
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
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(SecurityQuestionAuthUpdateApi::class.java)
    }
    viewModel {
        SecurityQuestionAuthUpdateViewModel(get(), get())
    }

}
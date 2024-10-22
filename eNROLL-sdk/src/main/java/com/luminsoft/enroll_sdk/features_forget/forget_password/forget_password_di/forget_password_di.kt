
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val forgetPasswordModule = module {
    single {
        ForgetPasswordUseCase(get())
    }
    single {
        GetDefaultEmailUseCase(get())
    }

    single {
        MailSendOTPUseCase(get())
    }

    single {
        ValidateOtpMailUseCase(get())
    }


    single<ForgetPasswordRemoteDataSource> {
        ForgetPasswordRemoteDataSourceImpl(get(), get())
    }


    single<ForgetPasswordRepository> {
       ForgetPasswordRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(ForgetPasswordApi::class.java)
    }
    viewModel {
        ForgetPasswordViewModel(get(), get(), get(), get())
    }


}
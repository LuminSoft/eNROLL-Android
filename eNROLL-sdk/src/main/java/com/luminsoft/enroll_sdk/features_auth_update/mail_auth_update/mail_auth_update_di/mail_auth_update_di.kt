
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mailAuthUpdateModule = module {
    single {
        MailAuthUpdateSendOTPUseCase(get())
    }
    single<MailAuthUpdateRemoteDataSource> {
        MailAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<MailAuthUpdateRepository> {
        MailAuthUpdateRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(MailAuthUpdateApi::class.java)
    }
    viewModel {
        MailAuthUpdateViewModel(get(), get())
    }


}
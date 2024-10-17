import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val phoneAuthUpdateModule = module {
    single {
        PhoneAuthUpdateSendOTPUseCase(get())
    }
    single<PhoneAuthUpdateRemoteDataSource> {
        PhoneAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<PhoneAuthUpdateRepository> {
        PhoneAuthUpdateRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(PhoneAuthUpdateApi::class.java)
    }
    viewModel {
        PhoneAuthUpdateViewModel(get(), get())
    }


}
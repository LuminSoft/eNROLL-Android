
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val passwordAuthUpdateModule = module {
    single {
        PasswordAuthUpdateUseCase(get())
    }
    single<PasswordAuthUpdateRemoteDataSource> {
        PasswordAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<PasswordAuthUpdateRepository> {
        PasswordAuthUpdateRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(PasswordAuthUpdateApi::class.java)
    }
    viewModel {
        PasswordAuthUpdateViewModel(get())
    }


}
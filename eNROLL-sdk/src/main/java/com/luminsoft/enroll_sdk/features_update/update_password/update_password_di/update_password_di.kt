
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val updatePasswordModule = module{
    single {
        UpdatePasswordUseCase(get())
    }
    single<UpdatePasswordRemoteDataSource> {
        UpdatePasswordRemoteDataSourceImpl(get(),get())
    }
    single<UpdatePasswordRepository> {
        UpdatePasswordRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(UpdatePasswordApi::class.java)
    }
    viewModel{
        UpdatePasswordViewModel(get())
    }


}
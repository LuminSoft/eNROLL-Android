
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val updateSecurityQuestionsModule = module {
    single {
        GetSecurityQuestionsUpdateUseCase(get())
    }
    single {
       UpdateSecurityQuestionsUseCase(get())
    }
    single<UpdateSecurityQuestionsRemoteDataSource> {
        UpdateSecurityQuestionsRemoteDataSourceImpl(get(), get())
    }
    single<UpdateSecurityQuestionsRepository> {
        UpdateSecurityQuestionsRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(UpdateSecurityQuestionsApi::class.java)
    }
    viewModel {
        UpdateSecurityQuestionsViewModel(get(), get(), get())
    }

}
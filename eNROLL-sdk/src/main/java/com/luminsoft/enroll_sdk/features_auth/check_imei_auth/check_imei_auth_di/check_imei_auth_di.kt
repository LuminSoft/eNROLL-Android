
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.dsl.module

val checkIMEIAuthModule = module {
    single {
        AuthCheckIMEIUseCase(get())
    }
    single<CheckIMEIAuthRemoteDataSource> {
        CheckIMEIAuthRemoteDataSourceImpl(get(), get())
    }
    single<CheckIMEIAuthRepository> {
        CheckIMEIAuthRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(CheckIMEIAuthApi::class.java)
    }
}
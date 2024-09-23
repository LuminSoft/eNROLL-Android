
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.dsl.module

val forgetLocationModule = module{
    single {
        ForgetLocationUseCase(get())
    }
    single<ForgetLocationRemoteDataSource> {
        ForgetLocationRemoteDataSourceImpl(get(),get())
    }
    single<ForgetLocationRepository> {
        ForgetLocationRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(ForgetLocationApi::class.java)
    }


}
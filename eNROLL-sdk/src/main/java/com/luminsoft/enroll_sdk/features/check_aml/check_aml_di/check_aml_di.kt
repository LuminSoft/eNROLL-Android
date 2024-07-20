
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.dsl.module

val checkAmlModule = module{
    single {
        CheckAmlUseCase(get())
    }
    single<CheckAmlRemoteDataSource> {
        CheckAmlRemoteDataSourceImpl(get(),get())
    }
    single<CheckAmlRepository> {
        CheckAmlRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(CheckAmlApi::class.java)
    }

}
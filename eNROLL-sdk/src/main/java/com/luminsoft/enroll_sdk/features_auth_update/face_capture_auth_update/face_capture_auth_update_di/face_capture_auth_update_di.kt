
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.dsl.module

val faceCaptureAuthUpdateModule = module {
    single {
        FaceCaptureAuthUpdateUseCase(get())
    }
    single<FaceCaptureAuthUpdateRemoteDataSource> {
        FaceCaptureAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<FaceCaptureAuthUpdateRepository> {
        FaceCaptureAuthUpdateRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(FaceCaptureAuthUpdateApi::class.java)
    }

}
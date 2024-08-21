
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth_update.face_capture_auth_update.face_capture_auth_update_data.face_capture_auth_update_api.FaceCaptureAuthUpdateApi
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
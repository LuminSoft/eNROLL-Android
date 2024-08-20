
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.dsl.module

val deviceIdAuthUpdateModule = module {
    single {
        CheckDeviceIdAuthUpdateUseCase(get())
    }
    single<DeviceIdAuthUpdateRemoteDataSource> {
        DeviceIdAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<DeviceIdAuthUpdateRepository> {
        DeviceIdAuthUpdateRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(DeviceIdAuthUpdateApi::class.java)
    }
}
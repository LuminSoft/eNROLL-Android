
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.dsl.module

val updateLocationModule = module{
    single {
        UpdateLocationUseCase(get())
    }
    single<UpdateLocationRemoteDataSource> {
        UpdateLocationRemoteDataSourceImpl(get(),get())
    }
    single<UpdateLocationRepository> {
        UpdateLocationRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(UpdateLocationApi::class.java)
    }

}

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_update.update_location.update_location_data.update_location_api.UpdateLocationApi
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
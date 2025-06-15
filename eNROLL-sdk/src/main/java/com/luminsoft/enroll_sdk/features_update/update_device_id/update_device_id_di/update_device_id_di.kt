
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val updateDeviceIdModule = module {
    single {
        UpdateDeviceIdUseCase(get())
    }
    single<UpdateDeviceIdRemoteDataSource> {
        UpdateDeviceIdRemoteDataSourceImpl(get(), get())
    }
    single<UpdateDeviceIdRepository> {
       UpdateDeviceIdRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(UpdateDeviceIdApi::class.java)
    }
    viewModel {
        UpdateDeviceIdViewModel(get(), context = androidContext())
    }
}
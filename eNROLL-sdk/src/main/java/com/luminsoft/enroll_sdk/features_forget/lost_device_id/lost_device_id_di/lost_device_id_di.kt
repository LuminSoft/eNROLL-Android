import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val lostDeviceIdModule = module {
    single {
        LostDeviceIdUseCase(get())
    }
    single<LostDeviceIdRemoteDataSource> {
        LostDeviceIdRemoteDataSourceImpl(get(), get())
    }
    single<LostDeviceIdRepository> {
        LostDeviceIdRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(LostDeviceIdApi::class.java)
    }
    viewModel {
        LostDeviceIdViewModel(get(), context = androidContext())
    }
}
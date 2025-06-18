import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val forgetLocationModule = module {
    single {
        ForgetLocationUseCase(get())
    }
    single<ForgetLocationRemoteDataSource> {
        ForgetLocationRemoteDataSourceImpl(get(), get())
    }
    single<ForgetLocationRepository> {
        ForgetLocationRepositoryImplementation(get())
    }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(ForgetLocationApi::class.java)
    }


}
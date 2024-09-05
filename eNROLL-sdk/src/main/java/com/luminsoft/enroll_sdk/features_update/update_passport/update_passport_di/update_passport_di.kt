
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.dsl.module

val updatePassportModule = module{
    single {
        UpdatePassportUploadImageUseCase(get())
    }
    single {
        UpdatePassportApproveUseCase(get())
    }
    single<UpdatePassportRemoteDataSource> {
        UpdatePassportRemoteDataSourceImpl(get(),get())
    }
    single<UpdatePassportRepository> {
        UpdatePassportRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(UpdatePassportApi::class.java)
    }

}
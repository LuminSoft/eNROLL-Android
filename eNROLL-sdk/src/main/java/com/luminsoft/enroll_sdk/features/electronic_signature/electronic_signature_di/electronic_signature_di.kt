
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.dsl.module

val electronicSignatureModule = module{
    single {
        InsertSignatureInfoUseCase(get())
    }
    single<ElectronicSignatureRemoteDataSource> {
        ElectronicSignatureRemoteDataSourceImpl(get(),get())
    }
    single<ElectronicSignatureRepository> {
        ElectronicSignatureRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(ElectronicSignatureApi::class.java)
    }

}
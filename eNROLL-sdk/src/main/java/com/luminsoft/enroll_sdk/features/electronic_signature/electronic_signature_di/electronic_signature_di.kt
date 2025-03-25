
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_api.ElectronicSignatureApi
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_domain.usecases.CheckUserHasNationalIdUseCase
import org.koin.dsl.module

val electronicSignatureModule = module{
    single {
        InsertSignatureInfoUseCase(get())
    }
    single {
        CheckUserHasNationalIdUseCase(get())
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
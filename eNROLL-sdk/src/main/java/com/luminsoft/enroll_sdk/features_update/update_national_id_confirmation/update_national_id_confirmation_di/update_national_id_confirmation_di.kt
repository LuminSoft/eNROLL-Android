
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.dsl.module

val updateNationalIdConfirmationModule = module{
    single {
        UpdatePersonalConfirmationUploadImageUseCase(get())
    }
    single {
        UpdatePersonalConfirmationApproveUseCase(get())
    }
    single {
        IsTranslationStepEnabledUseCase(get())
    }
    single<UpdateNationalIdConfirmationRemoteDataSource> {
        UpdateNationalIdConfirmationRemoteDataSourceImpl(get(),get())
    }
    single<UpdateNationalIdConfirmationRepository> {
        UpdateNationalIdConfirmationRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(UpdateNationalIdConfirmationApi::class.java)
    }


}
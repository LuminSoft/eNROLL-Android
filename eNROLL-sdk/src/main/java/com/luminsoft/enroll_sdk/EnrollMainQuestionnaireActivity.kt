package com.luminsoft.enroll_sdk

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.luminsoft.enroll_sdk.core.models.sdkModule
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.DynamicLocalizationManager
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.WifiService
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_di.questionnaireModule
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_presentation.ui.components.QuestionnaireScreenContent
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_presentation.view_model.QuestionnaireViewModel
import com.luminsoft.enroll_sdk.ui_components.theme.EKYCsDKTheme
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import java.util.Locale

@Suppress("DEPRECATION")
class EnrollMainQuestionnaireActivity : ComponentActivity() {
    override fun getResources(): Resources {
        return DynamicLocalizationManager.wrapResources(this, super.getResources())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getKoin(this)
        setupServices()
        setLocale()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        setContent {
            val questionnaireViewModel: QuestionnaireViewModel = koinViewModel()
            EKYCsDKTheme(
                appColors = EnrollSDK.appColors,
                appIcons = EnrollSDK.appIcons,
                dynamicColor = false
            ) {
                QuestionnaireScreenContent(
                    viewModel = questionnaireViewModel,
                    onExit = { finish() }
                )
            }
        }
    }

    private fun setupServices() {
        WifiService.instance.initializeWithApplicationContext(this)
        ResourceProvider.instance.initializeWithApplicationContext(this)
        RetroClient.setBaseUrl(EnrollSDK.getApisUrl())
    }

    private fun getKoin(activity: ComponentActivity): Koin {
        if (activity is KoinComponent) {
            return activity.getKoin()
        }

        val existingKoin = GlobalContext.getOrNull()
        if (existingKoin != null) {
            runCatching { loadKoinModules(questionnaireModule) }
            return existingKoin
        }

        return startKoin {
            androidContext(activity.applicationContext)
            modules(sdkModule)
            modules(questionnaireModule)
        }.koin
    }

    private fun setLocale() {
        val locale = EnrollSDK.localizationCode.name.let { Locale(it) }
        Locale.setDefault(locale)

        val config: Configuration = baseContext.resources.configuration
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }
}

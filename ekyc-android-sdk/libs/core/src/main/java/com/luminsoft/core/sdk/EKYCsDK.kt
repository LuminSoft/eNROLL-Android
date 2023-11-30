package com.luminsoft.core.sdk

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import com.luminsoft.core.sdk.model.EKYCCallback
import com.luminsoft.core.sdk.model.PaymentInfo
import java.util.Locale

object EKYCsDK {
    // this info related to organization
    internal var merchantCode = ""
    internal var merchantPhoneNumber = ""
    internal var hashKey = ""
    var merchantLogo:String? = null

    // this info related to every order
    internal var paymentInfo: PaymentInfo? = null
    private var environment = EkycEnvironment.STAGING
    private var localizationCode = LocalizationCode.EN
    internal var ekycCallback: EKYCCallback? = null

    internal fun getBaseUrl(): String {
        return if (environment == EkycEnvironment.STAGING)
            "https://sit.cowpay.me"
        else "https://sit.cowpay.me"
    }

    internal fun getApisUrl(): String {
        return if (environment == EkycEnvironment.STAGING)
            getBaseUrl() +":8000"
        else getBaseUrl() +":8000"
    }
    @Throws(Exception::class)
    fun init(
        merchantCode: String,
        merchantHash: String,
        merchantPhoneNumber: String,
        paymentInfo: PaymentInfo,
        environment: EkycEnvironment = EkycEnvironment.STAGING,
        localizationCode: LocalizationCode = LocalizationCode.EN,
        merchantLogo: String? = null,
    ) {
        if (merchantCode.isEmpty())
            throw Exception("Invalid Merchant Code")
        if (merchantHash.isEmpty())
            throw Exception("Invalid Hash Key")
        if (merchantPhoneNumber.isEmpty())
            throw Exception("Invalid Merchant PhoneNumber Key")
        if (!paymentInfo.customerMobile.matches(Regex("(01[0-9]{9})\$")))
            throw Exception("Invalid customer mobile number")
        EKYCsDK.environment = environment
        EKYCsDK.merchantCode = merchantCode
        hashKey = merchantHash
        EKYCsDK.paymentInfo = paymentInfo
        EKYCsDK.merchantPhoneNumber = merchantPhoneNumber
        EKYCsDK.localizationCode = localizationCode
        EKYCsDK.merchantLogo = merchantLogo

    }

    fun launch(
        activity: Activity,
        mainActivity: Activity,
        cowpayCallback: EKYCCallback? = null,
    ) {
        if (merchantCode.isEmpty())
            throw Exception("Invalid Merchant Code")
        if (hashKey.isEmpty())
            throw Exception("Invalid Hash Key")
        if (merchantPhoneNumber.isEmpty())
            throw Exception("Invalid Merchant PhoneNumber Key")
        if (paymentInfo == null)
            throw Exception("Invalid PaymentInfo")
        if (paymentInfo?.customerMobile?.matches(Regex("(01[0-9]{9})\$")) != true)
            throw Exception("Invalid customer mobile number")
        EKYCsDK.ekycCallback =cowpayCallback
        setLocale(localizationCode,activity)
        activity.startActivity(Intent(activity, mainActivity::class.java))
    }
    fun setLocale(lang: LocalizationCode, activity: Activity) {
        val locale = if(lang != LocalizationCode.AR){
            Locale("en")
        }else{
            Locale("ar")
        }

        val config: Configuration = activity.getBaseContext().getResources().getConfiguration()
        config.setLocale(locale)
        activity.getBaseContext().getResources().updateConfiguration(
            config,
            activity.getBaseContext().getResources().getDisplayMetrics()
        )
    }

}
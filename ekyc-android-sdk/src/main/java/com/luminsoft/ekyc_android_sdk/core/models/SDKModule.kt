package com.luminsoft.ekyc_android_sdk.core.models
import com.luminsoft.ekyc_android_sdk.core.network.BaseRemoteDataSource

import org.koin.dsl.module

val sdkModule = module{
    factory  {
        com.luminsoft.ekyc_android_sdk.core.network.BaseRemoteDataSource()
    }

}
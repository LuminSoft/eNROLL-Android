package com.luminsoft.ekyc_android_sdk.sdk
import com.luminsoft.core.network.BaseRemoteDataSource

import org.koin.dsl.module

val sdkModule = module{
    factory  {
        BaseRemoteDataSource()
    }

}
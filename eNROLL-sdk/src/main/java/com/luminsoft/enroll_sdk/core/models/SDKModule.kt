package com.luminsoft.enroll_sdk.core.models

import org.koin.dsl.module

val sdkModule = module{
    factory  {
        com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource()
    }

}
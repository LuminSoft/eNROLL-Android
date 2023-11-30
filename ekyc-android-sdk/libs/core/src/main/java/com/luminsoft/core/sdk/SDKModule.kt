package com.luminsoft.core.sdk
import com.luminsoft.core.network.BaseRemoteDataSource

import org.koin.dsl.module

val appModule = module{
    factory  {
        BaseRemoteDataSource()
    }

}
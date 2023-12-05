package com.luminsoft.ekyc_android_sdk.core.utils

interface  UseCase<T,Params>{
     suspend fun call(params:Params):T
}
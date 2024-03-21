package com.luminsoft.enroll_sdk.core.utils

interface  UseCase<T,Params>{
     suspend fun call(params:Params):T
}
package com.luminsoft.core.utils

interface  UseCase<T,Params>{
     suspend fun call(params:Params):T
}
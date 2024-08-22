package com.luminsoft.enroll_sdk.core.network

import com.google.gson.Gson
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.NoConnectionFailure
import com.luminsoft.enroll_sdk.core.failures.ServerFailure
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

class BaseRemoteDataSource {

    private val gson = Gson()

    suspend fun <T> apiRequest(
        call: suspend () -> Response<T>
    ): BaseResponse<Any> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body() ?: {}
                if ((response.code() == 200 || response.code() == 201 || response.code() == 204))
                    return BaseResponse.Success(body)
            } else {
                return if (response.code() == 401) {
                    BaseResponse.Error(
                        AuthFailure(response.errorBody().let {
                            gson.fromJson(
                                response.errorBody()?.string(),
                                ApiErrorResponse::class.java
                            )
                        })
                    )

                } else {
                    BaseResponse.Error(
                        ServerFailure(
                            gson.fromJson(
                                response.errorBody()?.string() ?: "Some Thing went wrong",
                                ApiErrorResponse::class.java
                            )
                        )
                    )
                }
            }

        } catch (e: Exception) {
            return when (e) {
                is NoConnectionException -> {
                    BaseResponse.Error(
                        NoConnectionFailure()
                    )
                }

                is UnknownHostException -> {
                    BaseResponse.Error(
                        NetworkFailure()
                    )
                }

                is ConnectException -> {
                    BaseResponse.Error(
                        NetworkFailure()
                    )
                }

                else -> {
                    BaseResponse.Error(
                        NetworkFailure(e.localizedMessage!!)
                    )
                }
            }
        }
        return BaseResponse.Error(
            NetworkFailure()
        )
    }

}
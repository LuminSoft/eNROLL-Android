package com.luminsoft.enroll_sdk.core.network

import com.google.gson.Gson
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.NoConnectionFailure
import com.luminsoft.enroll_sdk.core.failures.ServerFailure
import com.luminsoft.enroll_sdk.core.utils.EncryptionHelper
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

class BaseRemoteDataSource {

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
                val error = decryptErrorBody(response.errorBody())

                return if (response.code() == 401) {
                    BaseResponse.Error(
                        AuthFailure(error)
                    )

                } else {
                    BaseResponse.Error(
                        ServerFailure(error)
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

    private fun decryptErrorBody(errorBody: ResponseBody?): ApiErrorResponse {
        return try {
            val encryptedResponse =
                errorBody?.string() ?: return ApiErrorResponse(message = "Unknown error")
            val jsonObject =
                Gson().fromJson(encryptedResponse, com.google.gson.JsonObject::class.java)
            val encryptedData = jsonObject.get("Data")?.asString
                ?: return ApiErrorResponse(message = "Malformed error response")

            val decryptedJson = EncryptionHelper.decrypt(encryptedData)
            val decryptedString = decryptedJson.trim()

            // Some backends wrap JSON in a string, so decode twice if needed
            val finalJson = try {
                Gson().fromJson(decryptedString, String::class.java)
            } catch (e: Exception) {
                decryptedString // already plain JSON
            }

            Gson().fromJson(finalJson, ApiErrorResponse::class.java)
        } catch (e: Exception) {
            ApiErrorResponse(message = "Failed to parse error response: ${e.localizedMessage}")
        }
    }


}
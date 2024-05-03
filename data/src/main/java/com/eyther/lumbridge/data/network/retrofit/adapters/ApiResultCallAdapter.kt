package com.eyther.lumbridge.data.network.retrofit.adapters

import com.eyther.lumbridge.data.network.retrofit.model.ApiResult
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.net.ssl.HttpsURLConnection

/**
 * Custom Retrofit CallAdapter to handle the API calls errors and success states, by this we can
 * get response in form of [ApiResult]
 *
 */
class ApiResultCallAdapter<R>(
    private val responseType: Type,
    private val errorBodyConverter: Converter<ResponseBody, ErrorResponse>
) : CallAdapter<R, Call<ApiResult<R>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): Call<ApiResult<R>> {
        return AppResultCall(call, errorBodyConverter)
    }

    private class AppResultCall<R>(
        private val delegate: Call<R>,
        private val errorConverter: Converter<ResponseBody, ErrorResponse>
    ) : Call<ApiResult<R>> {

        override fun enqueue(callback: Callback<ApiResult<R>>) {
            delegate.enqueue(object : Callback<R> {
                override fun onResponse(call: Call<R>, response: Response<R>) {

                    val body = response.body()
                    val code = response.code()
                    val error = response.errorBody()

                    val result = if (response.isSuccessful) {
                        if (body != null) {
                            ApiResult.Success(body)
                        } else {
                            ApiResult.Error.RequestError(
                                exception = Throwable("Response body is null"),
                                code = code
                            )
                        }
                    } else if (code == HttpsURLConnection.HTTP_NOT_FOUND) {
                        ApiResult.Error.RequestError(
                            exception = Throwable(error?.string()),
                            code = code
                        )
                    } else {
                        val errorBody = when {
                            error == null || error.contentLength() == 0L -> null
                            else -> try {
                                errorConverter.convert(error)?.message
                            } catch (ex: Exception) {
                                ex.message
                            }
                        }

                        ApiResult.Error.RequestError(
                            exception = Throwable(errorBody),
                            code = code
                        )
                    }

                    callback.onResponse(this@AppResultCall, Response.success(result))
                }

                override fun onFailure(call: Call<R>, t: Throwable) {

                    val networkResponse = when (t) {
                        is IOException -> ApiResult.Error.NetworkError(t)
                        else -> ApiResult.Error.ServerError(t)
                    }

                    callback.onResponse(this@AppResultCall, Response.success(networkResponse))
                }
            })
        }

        // Other methods delegate to the original Call
        override fun execute(): Response<ApiResult<R>> {
            throw UnsupportedOperationException("execute not supported")
        }

        override fun isExecuted(): Boolean = delegate.isExecuted

        override fun cancel() = delegate.cancel()

        override fun isCanceled() = delegate.isCanceled

        override fun request(): Request = delegate.request()

        override fun timeout(): Timeout = delegate.timeout()

        override fun clone(): Call<ApiResult<R>> {
            return AppResultCall(delegate.clone(), errorConverter)
        }
    }
}

class AppResultCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<AppResult<<Foo>> or Call<AppResult<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != ApiResult::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) {
            "Response must be parameterized as AppResult<Foo> or AppResult<out Foo>"
        }

        val successBodyType = getParameterUpperBound(0, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<ErrorResponse>(
                null,
                ErrorResponse::class.java,
                annotations
            )

        return ApiResultCallAdapter<Any>(successBodyType, errorBodyConverter)
    }
}

/** Error response comes in case of Api failure (non 2xx code) */
data class ErrorResponse(
    val message: String
)
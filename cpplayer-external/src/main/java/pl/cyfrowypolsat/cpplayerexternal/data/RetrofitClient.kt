package pl.cyfrowypolsat.cpplayerexternal.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://b2c-mobile.redefine.pl/"
    private const val TAG = "CPPlayerOkHttp"

    val apiInterface: ApiInterface by lazy {
        retrofitClient
                .build()
                .create(ApiInterface::class.java)
    }

    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(createOkHttpClient())
    }

    private fun createOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message -> Timber.tag(TAG).d(message) }
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()
    }

}
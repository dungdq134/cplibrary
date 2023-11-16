package pl.cyfrowypolsat.cpdata.maintenance

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object MaintenanceClient {

    private const val BASE_URL = "http://b2c-mobile.redefine.pl/"
    private const val TAG = "MaintenanceOkHttp"

    val maintenanceApi: MaintenanceApi by lazy {
        retrofitClient
                .build()
                .create(MaintenanceApi::class.java)
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
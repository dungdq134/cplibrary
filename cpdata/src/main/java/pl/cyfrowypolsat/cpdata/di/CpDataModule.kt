package pl.cyfrowypolsat.cpdata.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.cyfrowypolsat.cpdata.api.auth.AuthApi
import pl.cyfrowypolsat.cpdata.api.common.clientcontext.ClientContextRefreshApi
import pl.cyfrowypolsat.cpdata.api.common.gson.CpidObjectDeserializer
import pl.cyfrowypolsat.cpdata.api.common.gson.PlayerOverlaysObjectDeserializer
import pl.cyfrowypolsat.cpdata.api.common.gson.ProductIdResultDeserializer
import pl.cyfrowypolsat.cpdata.api.common.interceptor.*
import pl.cyfrowypolsat.cpdata.api.common.interceptor.ClientContextInterceptor
import pl.cyfrowypolsat.cpdata.api.common.interceptor.GetApiHeaderInterceptor
import pl.cyfrowypolsat.cpdata.api.common.interceptor.HeaderInterceptor
import pl.cyfrowypolsat.cpdata.api.common.interceptor.SessionInterceptor
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCConverterFactory
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCGetParamsBuilder
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCRequestBuilder
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CpidObject
import pl.cyfrowypolsat.cpdata.api.common.session.AutoLoginApi
import pl.cyfrowypolsat.cpdata.api.common.session.SessionManager
import pl.cyfrowypolsat.cpdata.api.drm.DrmApi
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationApi
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.PlayerOverlays
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationGetApi
import pl.cyfrowypolsat.cpdata.api.payments.PaymentsApi
import pl.cyfrowypolsat.cpdata.api.payments.response.ProductIdResult
import pl.cyfrowypolsat.cpdata.api.system.SystemApi
import pl.cyfrowypolsat.cpdata.api.system.response.Services
import pl.cyfrowypolsat.cpdata.api.usercontent.UserContentApi
import pl.cyfrowypolsat.cpdata.common.ApplicationData
import pl.cyfrowypolsat.cpdata.files.FilesApi
import pl.cyfrowypolsat.cpdata.maintenance.MaintenanceInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CpDataModule {

    companion object {
        private const val TIMEOUT = 30
        private const val PRODUCTION_API_BASE_URL = "https://gm2.redefine.pl/rpc/"
        private const val STAGING_API_BASE_URL = "https://b2c-integration.redefine.pl/rpc/"
    }

    private fun getApiBaseUrl(applicationData: ApplicationData): String {
        return if (applicationData.isProduction) PRODUCTION_API_BASE_URL else STAGING_API_BASE_URL
    }

    @CpDataQualifier
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(CpidObject::class.java, CpidObjectDeserializer())
                .registerTypeAdapter(ProductIdResult::class.java, ProductIdResultDeserializer())
                .registerTypeAdapter(PlayerOverlays::class.java, PlayerOverlaysObjectDeserializer())
                .create()
    }


    // OkHttp
    @CpDataDefaultOkHttp
    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(
                        HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    @CpDataCommonOkHttp
    @Provides
    @Singleton
    fun provideCommonOkHttpClient(headerInterceptor: HeaderInterceptor,
                                  sessionInterceptor: SessionInterceptor,
                                  maintenanceInterceptor: MaintenanceInterceptor,
                                  clientContextInterceptor: ClientContextInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(RetryRequestInterceptor())
                .addInterceptor(sessionInterceptor)
                .addInterceptor(maintenanceInterceptor)
                .addInterceptor(clientContextInterceptor)
                .addInterceptor(
                        HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    @CpDataGetApiOkHttp
    @Provides
    @Singleton
    fun provideGetApiOkHttpClient(getApiHeaderInterceptor: GetApiHeaderInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(getApiHeaderInterceptor)
                .addInterceptor(RetryRequestInterceptor())
                .addInterceptor(
                        HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    @CpDataAutoLoginOkHttp
    @Provides
    @Singleton
    fun provideAutoLoginOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(RetryRequestInterceptor())
                .addInterceptor(
                        HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    @CpDataClientContextRefreshOkHttp
    @Provides
    @Singleton
    fun provideClientContextRefreshOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(RetryRequestInterceptor())
                .addInterceptor(
                        HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    @CpDataWebSocketOkHttp
    @Provides
    @Singleton
    fun provideWebSocketOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .pingInterval(10, TimeUnit.MINUTES)
                .addInterceptor(
                        HttpLoggingInterceptor { message -> Timber.tag("WebSocketOkHttp").d(message) }
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }


    // Retrofit
    @CpDataQualifier
    @Singleton
    @Provides
    fun provideAutoLoginApi(@CpDataQualifier gson: Gson,
                            @CpDataAutoLoginOkHttp autoLoginClient: OkHttpClient,
                            applicationData: ApplicationData,
                            jsonRPCRequestBuilder: JsonRPCRequestBuilder): AutoLoginApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(JsonRPCConverterFactory(Services.AUTH_NAMESPACE, jsonRPCRequestBuilder, null))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(autoLoginClient)
                .build()
                .create(AutoLoginApi::class.java)
    }

    @CpDataQualifier
    @Singleton
    @Provides
    fun provideClientContextRefreshApi(@CpDataQualifier gson: Gson,
                                       @CpDataClientContextRefreshOkHttp clientContextClient: OkHttpClient,
                                       jsonRPCRequestBuilder: JsonRPCRequestBuilder,
                                       applicationData: ApplicationData,
                                       sessionManager: SessionManager): ClientContextRefreshApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(JsonRPCConverterFactory(Services.SYSTEM_NAMESPACE, jsonRPCRequestBuilder, sessionManager))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(clientContextClient)
                .build()
                .create(ClientContextRefreshApi::class.java)
    }

    @CpDataQualifier
    @Singleton
    @Provides
    fun provideSystemApi(@CpDataQualifier gson: Gson,
                         @CpDataCommonOkHttp commonClient: OkHttpClient,
                         jsonRPCRequestBuilder: JsonRPCRequestBuilder,
                         applicationData: ApplicationData,
                         sessionManager: SessionManager): SystemApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(JsonRPCConverterFactory(Services.SYSTEM_NAMESPACE, jsonRPCRequestBuilder, sessionManager))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(commonClient)
                .build()
                .create(SystemApi::class.java)
    }

    @CpDataQualifier
    @Singleton
    @Provides
    fun provideAuthApi(@CpDataQualifier gson: Gson,
                       @CpDataCommonOkHttp commonClient: OkHttpClient,
                       jsonRPCRequestBuilder: JsonRPCRequestBuilder,
                       applicationData: ApplicationData,
                       sessionManager: SessionManager): AuthApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(JsonRPCConverterFactory(Services.AUTH_NAMESPACE, jsonRPCRequestBuilder, sessionManager))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(commonClient)
                .build()
                .create(AuthApi::class.java)
    }

    @CpDataQualifier
    @Singleton
    @Provides
    fun provideDrmApi(@CpDataQualifier gson: Gson,
                      @CpDataCommonOkHttp commonClient: OkHttpClient,
                      jsonRPCRequestBuilder: JsonRPCRequestBuilder,
                      applicationData: ApplicationData,
                      sessionManager: SessionManager): DrmApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(JsonRPCConverterFactory(Services.DRM_NAMESPACE, jsonRPCRequestBuilder, sessionManager))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(commonClient)
                .build()
                .create(DrmApi::class.java)
    }

    @CpDataQualifier
    @Singleton
    @Provides
    fun provideNavigationApi(@CpDataQualifier gson: Gson,
                             @CpDataCommonOkHttp commonClient: OkHttpClient,
                             jsonRPCRequestBuilder: JsonRPCRequestBuilder,
                             applicationData: ApplicationData,
                             sessionManager: SessionManager): NavigationApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(JsonRPCConverterFactory(Services.NAVIGATION_NAMESPACE, jsonRPCRequestBuilder, sessionManager))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(commonClient)
                .build()
                .create(NavigationApi::class.java)
    }

    @CpDataQualifier
    @Singleton
    @Provides
    fun provideNavigationGetApi(@CpDataQualifier gson: Gson,
                                @CpDataGetApiOkHttp commonClient: OkHttpClient,
                                jsonRPCGetParamsBuilder: JsonRPCGetParamsBuilder,
                                jsonRPCRequestBuilder: JsonRPCRequestBuilder,
                                applicationData: ApplicationData,
                                sessionManager: SessionManager): NavigationGetApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(JsonRPCConverterFactory(Services.NAVIGATION_NAMESPACE, jsonRPCRequestBuilder, sessionManager, jsonRPCGetParamsBuilder))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(commonClient)
                .build()
                .create(NavigationGetApi::class.java)
    }

    @CpDataQualifier
    @Singleton
    @Provides
    fun providePaymentsApi(@CpDataQualifier gson: Gson,
                           @CpDataCommonOkHttp commonClient: OkHttpClient,
                           jsonRPCRequestBuilder: JsonRPCRequestBuilder,
                           applicationData: ApplicationData,
                           sessionManager: SessionManager): PaymentsApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(JsonRPCConverterFactory(Services.PAYMENTS_NAMESPACE, jsonRPCRequestBuilder, sessionManager))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(commonClient)
                .build()
                .create(PaymentsApi::class.java)
    }

    @CpDataQualifier
    @Singleton
    @Provides
    fun provideUserContentApi(@CpDataQualifier gson: Gson,
                              @CpDataCommonOkHttp commonClient: OkHttpClient,
                              jsonRPCRequestBuilder: JsonRPCRequestBuilder,
                              applicationData: ApplicationData,
                              sessionManager: SessionManager): UserContentApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(JsonRPCConverterFactory(Services.USER_CONTENT_NAMESPACE, jsonRPCRequestBuilder, sessionManager))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(commonClient)
                .build()
                .create(UserContentApi::class.java)
    }

    @CpDataQualifier
    @Singleton
    @Provides
    fun provideFilesApi(@CpDataQualifier gson: Gson,
                        @CpDataDefaultOkHttp client: OkHttpClient,
                        applicationData: ApplicationData): FilesApi {
        return Retrofit.Builder()
                .baseUrl(getApiBaseUrl(applicationData))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(FilesApi::class.java)
    }

}
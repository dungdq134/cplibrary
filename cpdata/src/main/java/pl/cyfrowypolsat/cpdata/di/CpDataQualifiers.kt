package pl.cyfrowypolsat.cpdata.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class CpDataQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class CpDataDefaultOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class CpDataCommonOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class CpDataAutoLoginOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class CpDataClientContextRefreshOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class CpDataWebSocketOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class CpDataGetApiOkHttp

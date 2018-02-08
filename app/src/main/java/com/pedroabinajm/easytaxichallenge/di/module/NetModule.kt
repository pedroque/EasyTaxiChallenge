package com.pedroabinajm.easytaxichallenge.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.pedroabinajm.easytaxichallenge.BuildConfig
import com.pedroabinajm.easytaxichallenge.app.BaseApp
import com.pedroabinajm.easytaxichallenge.data.net.*
import com.pedroabinajm.easytaxichallenge.di.Environment
import com.pedroabinajm.easytaxichallenge.di.GsonTypeAdapter
import com.pedroabinajm.easytaxichallenge.di.LogInterceptor
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Environment("release")
    @Reusable
    internal fun getApiConfig(releaseApiConfig: ReleaseApiConfig): ApiConfig = releaseApiConfig

    @Provides
    @Environment("debug")
    @Reusable
    internal fun getDebugApiConfig(debugApiConfig: DebugApiConfig): ApiConfig = debugApiConfig

    @Provides
    @Environment("internal")
    @Reusable
    internal fun getInternalApiConfig(debugApiConfig: DebugApiConfig): ApiConfig = debugApiConfig

    @Provides
    @Reusable
    internal fun provideHttpCache(application: BaseApp): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Reusable
    @GsonTypeAdapter(Date::class)
    internal fun provideDateTypeAdapter(typeAdapter: DateTypeAdapter): TypeAdapter<*> = typeAdapter

    @Provides
    @Reusable
    internal fun provideGson(@GsonTypeAdapter(Date::class) dta: TypeAdapter<*>) =
            GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(object : TypeToken<Date>() {
                    }.type, dta)
                    .disableHtmlEscaping()
                    .create()

    @Provides
    @Reusable
    @LogInterceptor
    internal fun provideLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Reusable
    internal fun provideDefaultInterceptor(interceptor: DefaultInterceptor): Interceptor =
            interceptor

    @Provides
    @Reusable
    internal fun provideOkHttpClient(cache: Cache, interceptor: Interceptor, @Environment(BuildConfig.BUILD_TYPE) apiConfig: ApiConfig,
                                     @LogInterceptor logInterceptor: Interceptor): OkHttpClient {
        val b = OkHttpClient.Builder().cache(cache).addInterceptor(interceptor)
        b.connectTimeout(30, TimeUnit.SECONDS)
        if (apiConfig.log()) {
            b.addInterceptor(logInterceptor)
        }
        return b.build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient, @Environment(BuildConfig.BUILD_TYPE) apiConfig: ApiConfig) =
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(apiConfig.baseUrl)
                    .client(okHttpClient)
                    .build()
}

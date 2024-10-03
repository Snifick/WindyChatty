package com.flowtrust.windychatty.data.di

import android.content.Context
import android.content.SharedPreferences
import com.flowtrust.windychatty.data.AuthApi
import com.flowtrust.windychatty.data.UserApi
import com.flowtrust.windychatty.data.UserCountryInfoApi
import com.flowtrust.windychatty.data.countryData.CountryApi
import com.flowtrust.windychatty.data.network.AddCookiesInterceptor
import com.flowtrust.windychatty.data.network.BaseRetrofit
import com.flowtrust.windychatty.data.network.CountyRetrofit
import com.flowtrust.windychatty.data.network.ReceivedCookiesInterceptor
import com.flowtrust.windychatty.data.network.UserCountryRetrofit
import com.flowtrust.windychatty.domain.repository.AuthRepository
import com.flowtrust.windychatty.domain.repository.CountyRepository
import com.flowtrust.windychatty.domain.repository.UserRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {

    private const val baseUrl = "https://plannerok.ru/"
    private const val baseCountyUrl = "https://gist.githubusercontent.com/kcak11/4a2f22fb8422342b3b3daa7a1965f4e4/raw/6a23d2217b0476a326958f97cb1da1865af83a1f/" // Обновлено для API стран
    private const val userCountryInfoUrl = "https://ipinfo.io/"
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideReceivedCookiesInterceptor(
        @ApplicationContext context: Context
    ): ReceivedCookiesInterceptor = ReceivedCookiesInterceptor(context)

    @Provides
    fun provideAddCookiesInterceptor(
        @ApplicationContext context: Context
    ): AddCookiesInterceptor = AddCookiesInterceptor(context)

    @Provides
    fun provideOkHttpClient(
        logger: HttpLoggingInterceptor,
        cookieReceiver: ReceivedCookiesInterceptor,
        cookieAdder: AddCookiesInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(cookieReceiver)
        .addInterceptor(cookieAdder)
        .callTimeout(3, TimeUnit.MINUTES)
        .connectTimeout(3, TimeUnit.MINUTES)
        .readTimeout(3, TimeUnit.MINUTES)
        .writeTimeout(3, TimeUnit.MINUTES)
        .build()

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    // Основной Retrofit для AuthApi
    @Provides
    fun provideAuthRetrofit(
        moshi: Moshi, okHttpClient: OkHttpClient
    ): BaseRetrofit = BaseRetrofit(Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build())

    @Provides
    fun provideAuthApi(baseRetrofit: BaseRetrofit): AuthApi = baseRetrofit.retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("Windy", Context.MODE_PRIVATE)

    @Provides
    fun provideRepository(api: AuthApi, prefs: SharedPreferences): AuthRepository =
        AuthRepository(authApi = api, sharedPreferences = prefs)

    // Retrofit для CountryApi
    @Provides
    fun provideCountryRetrofit(
        moshi: Moshi, okHttpClient: OkHttpClient
    ): CountyRetrofit = CountyRetrofit(Retrofit.Builder()
        .baseUrl(baseCountyUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build())

    @Provides
    fun provideCountryApi(countryRetrofit: CountyRetrofit): CountryApi = countryRetrofit.retrofit.create(CountryApi::class.java)

    @Provides
    fun provideUserCountryInfoRetrofit(
        moshi: Moshi, okHttpClient: OkHttpClient
    ): UserCountryRetrofit = UserCountryRetrofit(Retrofit.Builder()
        .baseUrl(userCountryInfoUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build())

    @Provides
    fun provideUserCountryInfoApi(userCountryRetrofit: UserCountryRetrofit): UserCountryInfoApi = userCountryRetrofit.retrofit.create(UserCountryInfoApi::class.java)

    @Provides
    fun provideCountryRepository(api: CountryApi, userCountryInfoApi: UserCountryInfoApi): CountyRepository = CountyRepository(api,userCountryInfoApi)

    @Provides
    fun provideUserProfileApi(baseRetrofit: BaseRetrofit):UserApi = baseRetrofit.retrofit.create(UserApi::class.java)

    @Provides
    fun prodiveUserRepository(userApi: UserApi,sharedPreferences: SharedPreferences,authRepository: AuthRepository) = UserRepository(userApi,sharedPreferences, authRepository)


}

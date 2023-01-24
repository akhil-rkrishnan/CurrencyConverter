package app.android.currency_converter.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import app.android.currency_converter.BuildConfig
import app.android.currency_converter.application.EventBus
import app.android.currency_converter.data.helpers.NetworkConnection
import app.android.currency_converter.data.local.LocalConversion
import app.android.currency_converter.data.preferences.SharedPreferenceService
import app.android.currency_converter.data.remote.CurrencyConverterApi
import app.android.currency_converter.domain.repository.converter.ConverterRepository
import app.android.currency_converter.domain.repository.converter.ConverterRepositoryImpl
import app.android.currency_converter.domain.repository.localConversion.LocalConversionRepository
import app.android.currency_converter.domain.repository.localConversion.LocalConversionRepositoryImpl
import app.android.currency_converter.domain.repository.preferences.PreferenceRepository
import app.android.currency_converter.domain.repository.preferences.PreferenceRepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "CurrencyConverterESP",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    fun provideSharedPreferenceService(sharedPreferences: SharedPreferences): SharedPreferenceService =
        SharedPreferenceService(preferences = sharedPreferences)

    @Singleton
    @Provides
    fun providesEventBus() = EventBus()

}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideOkHttpClient(
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .writeTimeout(30L, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideRetrofitInstance(
        gson: Gson,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideCurrencyConverterApi(retrofit: Retrofit): CurrencyConverterApi =
        retrofit.create(CurrencyConverterApi::class.java)

    @Singleton
    @Provides
    fun provideNetworkConnection(@ApplicationContext context: Context) = NetworkConnection(context)
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideConverterRepository(
        currencyConverterApi: CurrencyConverterApi,
        preferenceRepo: PreferenceRepository
    ): ConverterRepository =
        ConverterRepositoryImpl(currencyConverterApi, preferenceRepository = preferenceRepo)

    @Singleton
    @Provides
    fun provideLocalConversion() = LocalConversion()

    @Provides
    fun provideLocalConversionRepository(localConversion: LocalConversion): LocalConversionRepository =
        LocalConversionRepositoryImpl(localConversion = localConversion)

    @Provides
    fun providePreferenceRepository(preferenceService: SharedPreferenceService): PreferenceRepository =
        PreferenceRepositoryImpl(preferenceService = preferenceService)
}
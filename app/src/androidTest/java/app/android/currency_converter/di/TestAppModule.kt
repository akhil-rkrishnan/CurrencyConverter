package app.android.currency_converter.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import app.android.currency_converter.application.EventBus
import app.android.currency_converter.data.local.LocalConversion
import app.android.currency_converter.data.preferences.SharedPreferenceService
import app.android.currency_converter.domain.repository.localConversion.LocalConversionRepository
import app.android.currency_converter.domain.repository.localConversion.LocalConversionRepositoryImpl
import app.android.currency_converter.domain.repository.preferences.PreferenceRepository
import app.android.currency_converter.domain.repository.preferences.PreferenceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TestSharedPreference

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TestSharedPreferenceService

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TestLocalConversionRepository

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TestLocalConversion

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TestPreferenceRepository

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TestEventBus

/**
* Test app module object for hilt testing
*/
@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Singleton
    @Provides
    @TestSharedPreference
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "CurrencyConverterESP_Test",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Singleton
    @Provides
    @TestLocalConversion
    fun provideLocalConversion() = LocalConversion()

    @Singleton
    @Provides
    @TestEventBus
    fun provideEventBus() = EventBus()

    @Provides
    @TestSharedPreferenceService
    fun provideSharedPreferenceService(@TestSharedPreference sharedPreferences: SharedPreferences): SharedPreferenceService =
        SharedPreferenceService(sharedPreferences)


    @Provides
    @TestLocalConversionRepository
    fun provideLocalConversionRepository(@TestLocalConversion localConversion: LocalConversion): LocalConversionRepository =
        LocalConversionRepositoryImpl(localConversion = localConversion)

    @Provides
    @TestPreferenceRepository
    fun providePreferenceRepository(@TestSharedPreferenceService preferenceService: SharedPreferenceService): PreferenceRepository =
        PreferenceRepositoryImpl(preferenceService = preferenceService)



}
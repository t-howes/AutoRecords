package com.duskencodings.autologs.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import com.duskencodings.autologs.database.CarDb
import com.duskencodings.autologs.database.CarWorkDb
import com.duskencodings.autologs.database.PreferencesDb
import com.duskencodings.autologs.repo.CarRepository
import com.duskencodings.autologs.repo.CarWorkRepository
import com.duskencodings.autologs.repo.PreferencesRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

  @Provides
  @Singleton
  internal fun provideCarRepo(context: Context, carDb: CarDb): CarRepository {
    return CarRepository(context, carDb)
  }

  @Provides
  @Singleton
  internal fun provideCarWorkRepo(context: Context, carWorkDb: CarWorkDb): CarWorkRepository {
    return CarWorkRepository(context, carWorkDb)
  }

  @Provides
  @Singleton
  internal fun providePrefsRepo(context: Context, preferencesDb: PreferencesDb): PreferencesRepository {
    return PreferencesRepository(context, preferencesDb)
  }
}
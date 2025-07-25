package com.chasewhiterabbit.dicengine.data.di

import android.content.Context
import androidx.room.Room
import com.chasewhiterabbit.dicengine.data.local.AppDatabase
import com.chasewhiterabbit.dicengine.data.local.DiceRollDao
import com.chasewhiterabbit.dicengine.data.repository.DiceRollRepository
import com.chasewhiterabbit.dicengine.domain.engine.DiceEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object TestDataModule {
    
    @Provides
    @Singleton
    fun provideInMemoryDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideDiceRollDao(database: AppDatabase): DiceRollDao {
        return database.diceRollDao()
    }

    @Provides
    @Singleton
    fun provideDiceRollRepository(
        diceRollDao: DiceRollDao,
        diceEngine: DiceEngine
    ): DiceRollRepository {
        return DiceRollRepository(diceRollDao, diceEngine)
    }
}

package com.chasewhiterabbit.dicengine.di

import android.content.Context
import androidx.room.Room
import com.chasewhiterabbit.dicengine.data.AppDatabase
import com.chasewhiterabbit.dicengine.data.DiceRollDao
import com.chasewhiterabbit.dicengine.data.DiceRollRepository
import com.chasewhiterabbit.dicengine.engine.DiceEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {
    
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
    fun provideMockDiceEngine(): DiceEngine {
        return DiceEngine().apply {
            // Configure for deterministic test results
            setSeed(123L)
        }
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

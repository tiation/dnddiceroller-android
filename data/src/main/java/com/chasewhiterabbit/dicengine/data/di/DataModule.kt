package com.chasewhiterabbit.dicengine.data.di

import android.content.Context
import androidx.room.Room
import com.chasewhiterabbit.dicengine.data.local.AppDatabase
import com.chasewhiterabbit.dicengine.data.local.DiceRollDao
import com.chasewhiterabbit.dicengine.data.repository.DiceRollRepository
import com.chasewhiterabbit.dicengine.domain.engine.DiceEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "dice_rolls_db"
        ).build()
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

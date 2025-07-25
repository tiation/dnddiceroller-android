package com.chasewhiterabbit.dicengine.domain.di

import com.chasewhiterabbit.dicengine.domain.engine.DiceEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    
    @Provides
    @Singleton
    fun provideDiceEngine(): DiceEngine {
        return DiceEngine()
    }
}

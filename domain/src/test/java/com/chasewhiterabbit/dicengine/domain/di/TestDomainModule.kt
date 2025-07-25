package com.chasewhiterabbit.dicengine.domain.di

import com.chasewhiterabbit.dicengine.domain.engine.DiceEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DomainModule::class]
)
object TestDomainModule {
    
    @Provides
    @Singleton
    fun provideMockDiceEngine(): DiceEngine {
        return DiceEngine().apply {
            // Configure for deterministic test results
            setSeed(123L)
        }
    }
}

package com.abel.dynamoxquiz.di

import com.abel.dynamoxquiz.data.repository.QuizRepositoryImpl
import com.abel.dynamoxquiz.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

@Binds
@Singleton
abstract fun bindQuizRepository(
    repositoryImpl: QuizRepositoryImpl
): QuizRepository
}
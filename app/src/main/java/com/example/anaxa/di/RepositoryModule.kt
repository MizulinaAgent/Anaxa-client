package com.example.anaxa.di

import com.example.anaxa.data.repository.AuthRepositoryImpl
import com.example.anaxa.data.repository.GamesRepositoryImpl
import com.example.anaxa.data.repository.LotsRepositoryImpl
import com.example.anaxa.data.repository.MessagesRepositoryImpl
import com.example.anaxa.data.repository.OrdersRepositoryImpl
import com.example.anaxa.data.repository.ReviewsRepositoryImpl
import com.example.anaxa.domain.repository.AuthRepository
import com.example.anaxa.domain.repository.GamesRepository
import com.example.anaxa.domain.repository.LotsRepository
import com.example.anaxa.domain.repository.MessagesRepository
import com.example.anaxa.domain.repository.OrdersRepository
import com.example.anaxa.domain.repository.ReviewsRepository
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
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGamesRepository(impl: GamesRepositoryImpl): GamesRepository

    @Binds
    @Singleton
    abstract fun bindLotsRepository(impl: LotsRepositoryImpl): LotsRepository

    @Binds
    @Singleton
    abstract fun bindOrdersRepository(impl: OrdersRepositoryImpl): OrdersRepository

    @Binds
    @Singleton
    abstract fun bindMessagesRepository(impl: MessagesRepositoryImpl): MessagesRepository

    @Binds
    @Singleton
    abstract fun bindReviewsRepository(impl: ReviewsRepositoryImpl): ReviewsRepository
}

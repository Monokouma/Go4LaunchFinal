package com.despaircorp.data.utils

import com.despaircorp.data.user.UserRepositoryFirestore
import com.despaircorp.domain.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class DataBindModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryFirestore): UserRepository
}
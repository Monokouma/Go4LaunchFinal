package com.despaircorp.data.utils

import com.despaircorp.data.LocationProviderRepository
import com.despaircorp.data.authentication.AuthenticationRepositoryFirebase
import com.despaircorp.data.coworkers.CoworkersFirebaseRepository
import com.despaircorp.data.restaurants.details.RestaurantDetailsRetrofitRepository
import com.despaircorp.data.restaurants.list.RestaurantsRepositoryRetrofit
import com.despaircorp.data.user.UserRepositoryFirestore
import com.despaircorp.domain.authentication.AuthenticationRepository
import com.despaircorp.domain.coworkers.CoworkersRepository
import com.despaircorp.domain.location.LocationRepository
import com.despaircorp.domain.restaurants.details.RestaurantDetailsRepository
import com.despaircorp.domain.restaurants.list.RestaurantsRepository
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
    
    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(impl: AuthenticationRepositoryFirebase): AuthenticationRepository
    
    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationProviderRepository): LocationRepository
    
    @Binds
    @Singleton
    abstract fun bindRestaurantsRepository(impl: RestaurantsRepositoryRetrofit): RestaurantsRepository
    
    @Binds
    @Singleton
    abstract fun bindCoworkersRepository(impl: CoworkersFirebaseRepository): CoworkersRepository
    
    @Binds
    @Singleton
    abstract fun bindRestaurantDetails(impl: RestaurantDetailsRetrofitRepository): RestaurantDetailsRepository
}
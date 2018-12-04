package com.airline.android.di

import com.airline.android.vm.HomeViewModel
import com.airline.android.vm.RoutesViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Dagger component for dependency injection in various view models.
 */
@Singleton
@Component(modules = [NetModule::class])
interface ViewModelInjector {
    /**
     * Injector for [HomeViewModel]
     */
    fun inject(homeViewModel: HomeViewModel)
    /**
     * Injector for [RoutesViewModel]
     */
    fun inject(routesViewModel: RoutesViewModel)
}

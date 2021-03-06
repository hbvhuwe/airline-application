package com.airline.android.vm

import com.airline.android.net.AirlineApi
import javax.inject.Inject

/**
 * Subclass of [BaseViewModel] for home page
 */
class HomeViewModel : BaseViewModel() {
    @Inject
    lateinit var airlineApi: AirlineApi
}

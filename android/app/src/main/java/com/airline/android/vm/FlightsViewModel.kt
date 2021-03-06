package com.airline.android.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airline.android.model.Flight
import com.airline.android.model.JsendFail
import com.airline.android.model.JsendResponse
import com.airline.android.net.AirlineApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FlightsViewModel(var flightId: Int = 0) : BaseViewModel() {
    @Inject
    lateinit var api: AirlineApi
    val routeId = MutableLiveData<Int>()
    private lateinit var flights: MutableLiveData<List<Flight>>
    private lateinit var flightsForRoute: MutableLiveData<List<Flight>>
    private lateinit var flight: MutableLiveData<Flight>

    lateinit var errorCallback: (message: String?) -> Unit

    fun getAllFlights(): LiveData<List<Flight>> {
        if (!::flights.isInitialized) {
            flights = MutableLiveData()
            loadAllFlights()
        }
        return flights
    }

    private fun loadAllFlights() {
        val callback = object : Callback<JsendResponse> {
            override fun onFailure(call: Call<JsendResponse>, t: Throwable) {
                errorCallback("Network error: ${t.message}")
            }

            override fun onResponse(call: Call<JsendResponse>, response: Response<JsendResponse>) {
                val body = response.body()
                if (body?.status == "success") {
                    flights.value = body.listData as List<Flight>?
                } else {
                    if (body?.data != null) {
                        errorCallback((body.data as JsendFail).message)
                    } else {
                        errorCallback(body?.message)
                    }
                }
            }
        }
        api.getFlights().enqueue(callback)
    }

    fun getFlightsForRoute(): LiveData<List<Flight>> {
        if (!::flightsForRoute.isInitialized) {
            flightsForRoute = MutableLiveData()
            loadFlightsForRoute(routeId.value!!)
        }
        return flightsForRoute
    }

    private fun loadFlightsForRoute(routeId: Int) {
        val callback = object : Callback<JsendResponse> {
            override fun onFailure(call: Call<JsendResponse>, t: Throwable) {
                errorCallback("Network error: ${t.message}")
            }

            override fun onResponse(call: Call<JsendResponse>, response: Response<JsendResponse>) {
                val body = response.body()
                if (body != null) {
                    when(body.status) {
                        "success" -> flightsForRoute.value = body.listData as List<Flight>?
                        "fail" -> errorCallback((body.data as JsendFail).message)
                        "error" -> errorCallback(body.message)
                    }
                }
            }
        }
        api.getFlightsForRoute(routeId).enqueue(callback)
    }

    fun getFlight(): LiveData<Flight> {
        if (!::flight.isInitialized) {
            flight = MutableLiveData()
            loadFlight(flightId)
        }
        return flight
    }

    private fun loadFlight(flightId: Int) {
        val callback= object : Callback<JsendResponse> {
            override fun onFailure(call: Call<JsendResponse>, t: Throwable) {
                errorCallback("Network error: ${t.message}")
            }

            override fun onResponse(call: Call<JsendResponse>, response: Response<JsendResponse>) {
                val body = response.body()
                if (body != null) {
                    when(body.status) {
                        "success" -> flight.value = body.data as Flight
                        "fail" -> errorCallback((body.data as JsendFail).message)
                        "error" -> errorCallback(body.message)
                    }
                }
            }
        }
        api.getFlight(flightId).enqueue(callback)
    }

    fun searchFlights(departure: String?, arrival: String, sort: String, order: String) {
        val callback = object : Callback<JsendResponse> {
            override fun onFailure(call: Call<JsendResponse>, t: Throwable) {
                errorCallback("Network error: ${t.message}")
            }

            override fun onResponse(call: Call<JsendResponse>, response: Response<JsendResponse>) {
                val body = response.body()
                if (body?.status == "success") {
                    flights.value = body.listData as List<Flight>?
                } else {
                    if (body?.data != null) {
                        errorCallback((body.data as JsendFail).message)
                    } else {
                        errorCallback(body?.message)
                    }
                }
            }
        }
        api.searchFlights(departure, arrival, sort, order).enqueue(callback)
    }
}
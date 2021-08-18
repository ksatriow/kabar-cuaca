package com.hellu.kabarcuaca.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellu.kabarcuaca.data.repository.WeatherRepository
import com.hellu.kabarcuaca.entity.WeatherDetails
import com.hellu.kabarcuaca.entity.WeatherResponse
import com.hellu.kabarcuaca.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel (private val repository: WeatherRepository) :
    ViewModel() {

    private val _weatherLiveData =
        MutableLiveData<Event<State<WeatherDetails>>>()
    val weatherLiveData: LiveData<Event<State<WeatherDetails>>>
        get() = _weatherLiveData

    private val _weatherDetailListLiveData =
        MutableLiveData<Event<State<List<WeatherDetails>>>>()
    val weatherDetailListLiveData: LiveData<Event<State<List<WeatherDetails>>>>
        get() = _weatherDetailListLiveData

    private lateinit var weatherResponse: WeatherResponse

    private fun findCityWeather(cityName: String) {
        _weatherLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherResponse =
                    repository.findCityWeather(cityName)
                insertWeatherIntoDatabase(weatherResponse)
                withContext(Dispatchers.Main) {
                    val weatherDetail = WeatherDetails()
                    weatherDetail.icon = weatherResponse.weather.first().icon
                    weatherDetail.cityName = weatherResponse.name
                    weatherDetail.countryName = weatherResponse.sys.country
                    weatherDetail.temp = weatherResponse.main.temp
                    _weatherLiveData.postValue(
                        Event(
                            State.success(
                                weatherDetail
                            )
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(
                        Event(
                            State.error(
                                e.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private suspend fun insertWeatherIntoDatabase(weatherResponse: WeatherResponse) {
        val weatherDetail = WeatherDetails()
        weatherDetail.id = weatherResponse.id
        weatherDetail.icon = weatherResponse.weather.first().icon
        weatherDetail.cityName = weatherResponse.name.toLowerCase()
        weatherDetail.countryName = weatherResponse.sys.country
        weatherDetail.temp = weatherResponse.main.temp
        weatherDetail.dateTime = Utils.getCurrentDateTime(Constants.DATE_FORMAT_1)
        repository.addWeather(weatherDetail)
    }

    fun getWeatherFromDatabase(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetail = repository.fetchWeatherDetail(cityName.toLowerCase())
            withContext(Dispatchers.Main) {
                if (weatherDetail != null) {
                    // Return true of current date and time is greater then the saved date and time of weather searched
                    if (Utils.isTimeExpired(weatherDetail.dateTime)) {
                        findCityWeather(cityName)
                    } else {
                        _weatherLiveData.postValue(
                            Event(
                                State.success(
                                    weatherDetail
                                )
                            )
                        )
                    }

                } else {
                    findCityWeather(cityName)
                }

            }
        }
    }

    fun getAllWeatherFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetailList = repository.fetchAllWeatherDetails()
            withContext(Dispatchers.Main) {
                _weatherDetailListLiveData.postValue(
                    Event(
                        State.success(weatherDetailList)
                    )
                )
            }
        }
    }
}
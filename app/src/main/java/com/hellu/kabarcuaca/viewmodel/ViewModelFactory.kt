package com.hellu.kabarcuaca.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hellu.kabarcuaca.data.repository.WeatherRepository
import com.hellu.kabarcuaca.ui.history.HistoryViewModel
import com.hellu.kabarcuaca.ui.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory (
    private val repository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
        return HistoryViewModel(repository) as T
    }
}

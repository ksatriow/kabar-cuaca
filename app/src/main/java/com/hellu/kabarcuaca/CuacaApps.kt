package com.hellu.kabarcuaca

import android.app.Application
import com.hellu.kabarcuaca.data.local.room.WeatherDatabase
import com.hellu.kabarcuaca.data.repository.WeatherRepository
import com.hellu.kabarcuaca.network.ApiServices
import com.hellu.kabarcuaca.network.NetworkInterceptor
import com.hellu.kabarcuaca.viewmodel.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CuacaApps : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@CuacaApps))

        bind() from singleton { NetworkInterceptor(instance()) }
        bind() from singleton { ApiServices(instance()) }
        bind() from singleton { WeatherRepository(instance(), instance()) }
        bind() from provider { ViewModelFactory(instance()) }
        bind() from provider { WeatherDatabase(instance()) }
    }


}
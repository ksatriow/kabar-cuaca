package com.hellu.kabarcuaca.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellu.kabarcuaca.R
import com.hellu.kabarcuaca.adapter.HistoryAdapter
import com.hellu.kabarcuaca.databinding.ActivityHistoryBinding
import com.hellu.kabarcuaca.ui.main.MainViewModel
import com.hellu.kabarcuaca.utils.*
import com.hellu.kabarcuaca.viewmodel.ViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class HistoryActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var dataBind: ActivityHistoryBinding
    private val factory: ViewModelFactory by instance()

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    lateinit var searchHistoryAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_history)
        supportActionBar?.hide()
        setupUI()
        observeAPICall()
    }

    private fun setupUI() {
        initializeRecyclerView()
        viewModel.getAllWeatherFromDatabase()
    }

    private fun initializeRecyclerView() {
        searchHistoryAdapter = HistoryAdapter()
        val mLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        dataBind.recyclerViewHistoryWeather.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = searchHistoryAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeAPICall() {
        viewModel.weatherDetailListLiveData.observe(this, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    if (state.data.isEmpty()) {
                        dataBind.recyclerViewHistoryWeather.hide()
                    } else {
                        dataBind.recyclerViewHistoryWeather.show()
                        searchHistoryAdapter.setData(state.data)
                    }
                }
                is State.Error -> {
                    showToast(state.message)
                }
            }
        })
    }
}
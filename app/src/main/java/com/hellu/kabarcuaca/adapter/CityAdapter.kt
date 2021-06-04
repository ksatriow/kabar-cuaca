package com.hellu.kabarcuaca.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellu.kabarcuaca.databinding.ListCityWeatherBinding
import com.hellu.kabarcuaca.entity.WeatherDetails
import androidx.databinding.DataBindingUtil
import com.hellu.kabarcuaca.R
import com.hellu.kabarcuaca.utils.Constants
import com.hellu.kabarcuaca.utils.Utils

class CityAdapter :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    private val weatherDetailList = ArrayList<WeatherDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListCityWeatherBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_city_weather,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(weatherDetailList[position])
    }

    override fun getItemCount(): Int = weatherDetailList.size

    fun setData(
        newWeatherDetail: List<WeatherDetails>
    ) {
        weatherDetailList.clear()
        weatherDetailList.addAll(newWeatherDetail)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListCityWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(weatherDetail: WeatherDetails) {
            binding.apply {
                val iconCode = weatherDetail.icon?.replace("n", "d")
                Utils.setGlideImage(
                    imageWeatherSymbol,
                    Constants.WEATHER_API_IMAGE_ENDPOINT + "${iconCode}@4x.png"
                )
                textCityName.text =
                    "${weatherDetail.cityName?.capitalize()}, ${weatherDetail.countryName}"
                textTemperature.text = weatherDetail.temp.toString()
                textDateTime.text = weatherDetail.dateTime
            }
        }
    }
}
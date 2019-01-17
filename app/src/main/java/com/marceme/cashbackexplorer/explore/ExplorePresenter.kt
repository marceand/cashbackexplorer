package com.marceme.cashbackexplorer.explore

import com.marceme.cashbackexplorer.model.VenuesResponse
import com.marceme.cashbackexplorer.network.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExplorePresenter(private val exploreView: ExploreView, private val apiService: APIService){

    fun getVenues(city: String) {

        exploreView.showProgress()

        val token = exploreView.getToken()
        apiService.getVenues(token, city).enqueue(object : Callback<VenuesResponse> {
            override fun onResponse(call: Call<VenuesResponse>, response: Response<VenuesResponse>) {
                exploreView.hideProgress()
                if(response.isSuccessful){
                    val venues = response.body()?.venues ?: mutableListOf()
                    exploreView.showVenues(venues)
                }else{
                    exploreView.showVenuesError()
                }
            }

            override fun onFailure(call: Call<VenuesResponse>, t: Throwable) {
                exploreView.hideProgress()
                exploreView.showVenuesError()
            }
        })
    }


}

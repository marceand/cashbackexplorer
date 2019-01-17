package com.marceme.cashbackexplorer.explore

import com.marceme.cashbackexplorer.model.Venue

interface ExploreView{

    fun getToken(): String

    fun showProgress()

    fun hideProgress()

    fun showVenues(venues: List<Venue>)

    fun showVenuesError()
}

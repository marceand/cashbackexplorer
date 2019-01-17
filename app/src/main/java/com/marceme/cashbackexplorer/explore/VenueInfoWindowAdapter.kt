package com.marceme.cashbackexplorer.explore

import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.marceme.cashbackexplorer.R
import android.app.Activity
import android.widget.TextView
import com.marceme.cashbackexplorer.model.Venue


class VenueInfoWindowAdapter(val context: Context): GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View {
        val view = (context as Activity).layoutInflater.inflate(R.layout.map_venue_infowindow, null)
        val venue = marker.getTag() as Venue

        view.findViewById<TextView>(R.id.venue_name).text = venue.name
        view.findViewById<TextView>(R.id.city).text = venue.city
        view.findViewById<TextView>(R.id.cashback).text = String.format(context.getString(R.string.cashback_percentage), venue.cashback)

        return view
    }

    override fun getInfoWindow(p0: Marker): View? {
        return null
    }
}
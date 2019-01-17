package com.marceme.cashbackexplorer.explore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.marceme.cashbackexplorer.R
import com.marceme.cashbackexplorer.login.PREF_FILE_NAME
import com.marceme.cashbackexplorer.login.PREF_KEY_TOKEN
import com.marceme.cashbackexplorer.model.Venue
import com.marceme.cashbackexplorer.network.APIServiceFactory
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import java.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import android.support.v4.app.ActivityCompat
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_explorer.*

class ExplorerActivity : AppCompatActivity(), OnMapReadyCallback, ExploreView {

    private lateinit var iconGenerator: IconGenerator
    private lateinit var explorePresenter: ExplorePresenter
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explorer)

        iconGenerator = iconGenerator()
        explorePresenter = ExplorePresenter(this, APIServiceFactory.makeApiService())

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val customInfoWindow = VenueInfoWindowAdapter(this)
        map.setInfoWindowAdapter(customInfoWindow)
        getVenuesOnLastLocation()
    }

    override fun showProgress() {
        progressBar.visibility = VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = GONE
    }

    override fun showVenues(venues: List<Venue>) {
        for(venue in venues){
            showVenueMarkerOnMap(venue)
        }
    }

    override fun getToken(): String {
        val preference = this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        return preference.getString(PREF_KEY_TOKEN, "")
    }

    override fun showVenuesError() {
        Toast.makeText(this, getString(R.string.error_getting_venues), Toast.LENGTH_LONG).show()
    }

    private fun getVenuesOnLastLocation() {

        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
            return
        }

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val city = getCity(location)
                    explorePresenter.getVenues(city)
                    zoomToLastLocation(location)
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }

        map.isMyLocationEnabled = true;
    }

    private fun getCity(location: Location): String{
        val geocoder = Geocoder(this, Locale.getDefault());
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
        return addresses.get(0).locality
    }

    private fun zoomToLastLocation(location: Location) {
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude))
            .zoom(10f)
            .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun showVenueMarkerOnMap(venue: Venue){
        val latLonOfVenue = LatLng(venue.lat, venue.long)
        val markerOptions = MarkerOptions().apply { position(latLonOfVenue)}
        val venueMarker =  map.addMarker(markerOptions).apply { tag = venue }

        val cashbackIcon = makeCashbackIcon(venue.cashback)
        venueMarker.setIcon(cashbackIcon)
    }

    private fun makeCashbackIcon(cashback: Double): BitmapDescriptor {
        val cashbackInPecentage = String.format(getString(R.string.cashback_format), cashback)
        val cashbackIcon = iconGenerator.makeIcon(cashbackInPecentage)
        return BitmapDescriptorFactory.fromBitmap(cashbackIcon)
    }

    private fun iconGenerator(): IconGenerator {
        val markerColor = ContextCompat.getColor(this, R.color.markerColor)
        return IconGenerator(this).apply { setColor(markerColor)
                                                   setTextAppearance(R.style.markerText)}
    }


}

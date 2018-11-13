package com.rommelrico.hikerswatch

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat

class MainActivity : AppCompatActivity() {

    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

        locationListener = object: LocationListener {

            /**
             * Called when the location has changed.
             *
             *
             *  There are no restrictions on the use of the supplied Location object.
             *
             * @param location The new location, as a Location object.
             */
            override fun onLocationChanged(location: Location?) { }

            /**
             * Called when the provider status changes. This method is called when
             * a provider is unable to fetch a location or if the provider has recently
             * become available after a period of unavailability.
             *
             * @param provider the name of the location provider associated with this
             * update.
             * @param status [LocationProvider.OUT_OF_SERVICE] if the
             * provider is out of service, and this is not expected to change in the
             * near future; [LocationProvider.TEMPORARILY_UNAVAILABLE] if
             * the provider is temporarily unavailable but is expected to be available
             * shortly; and [LocationProvider.AVAILABLE] if the
             * provider is currently available.
             * @param extras an optional Bundle which will contain provider specific
             * status variables.
             *
             *
             *  A number of common key/value pairs for the extras Bundle are listed
             * below. Providers that use any of the keys on this list must
             * provide the corresponding value as described below.
             *
             *
             *  *  satellites - the number of satellites used to derive the fix
             *
             */
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) { }

            /**
             * Called when the provider is enabled by the user.
             *
             * @param provider the name of the location provider associated with this
             * update.
             */
            override fun onProviderEnabled(provider: String?) { }

            /**
             * Called when the provider is disabled by the user. If requestLocationUpdates
             * is called on an already disabled provider, this method is called
             * immediately.
             *
             * @param provider the name of the location provider associated with this
             * update.
             */
            override fun onProviderDisabled(provider: String?) { }

        }

    } // end onCreate

    fun startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

}

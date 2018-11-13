package com.rommelrico.hikerswatch

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.TextView
import java.io.IOException
import java.util.*

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
            override fun onLocationChanged(location: Location?) {
                updateLocationInfo(location)
            }

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

        } // end locationListener

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

            val location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                updateLocationInfo(location)
            }
        }

    } // end onCreate

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
            startListening()
        }
    }

    fun startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    fun updateLocationInfo(location: Location?) {

        val latTextView = findViewById<TextView>(R.id.latTextView)
        val lonTextView = findViewById<TextView>(R.id.lonTextView)
        val altTextView = findViewById<TextView>(R.id.altTextView)
        val accTextView = findViewById<TextView>(R.id.accTextView)

        latTextView.text = "Latitude: ${location?.latitude}"
        lonTextView.text = "Longitude: ${location?.longitude}"
        altTextView.text = "Altitude: ${location?.altitude}"
        accTextView.text = "Accuracy: ${location?.accuracy}"

        val geocoder = Geocoder(applicationContext, Locale.getDefault())

        try {
            var address = "Could not find address"
            val listAddresses = geocoder.getFromLocation(location?.latitude!!, location.longitude, 1)

            if (listAddresses != null && listAddresses.size > 0) {
                address = "Address: \n"

                if (listAddresses[0].subThoroughfare != null) {
                    address += listAddresses[0].subThoroughfare + " "
                }

                if (listAddresses[0].thoroughfare != null) {
                    address += listAddresses[0].thoroughfare + "\n"
                }

                if (listAddresses[0].locality != null) {
                    address += listAddresses[0].locality + "\n"
                }

                if (listAddresses[0].postalCode != null) {
                    address += listAddresses[0].postalCode + "\n"
                }

                if (listAddresses[0].countryName != null) {
                    address += listAddresses[0].countryName + "\n"
                }

            }

            val addressTextView = findViewById<TextView>(R.id.addressTextView)
            addressTextView.text = address
        } catch (e: IOException) {
            e.printStackTrace()
        }
    } // end updateLocation

}

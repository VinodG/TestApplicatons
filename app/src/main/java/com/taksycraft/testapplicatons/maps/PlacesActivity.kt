package com.taksycraft.testapplicatons.maps

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ActivityPlacesBinding


class PlacesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacesBinding

    private val AUTOCOMPLETE_REQUEST_CODE: Int = 100
    val API_KEY = "AIzaSyB3rofoeKiv7spxC6KwPJuoovctLQor8K0"
    val TAG = "PlacesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_places)
        binding.etAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                p0?.let { findAddress(it.toString()) }
            }
        })

        validateUsing_libphonenumber("91", "986665432")

        Places.initialize(applicationContext, API_KEY)// Initialize the SDK
        val placesClient = Places.createClient(this)// Create a new PlacesClient instance


    }

    private fun findAddress(adddess: String) {
        val fields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        binding.etAddress.setText(place?.name ?: "")
                        Log.i(TAG, "Place: ${place.name} - ${place.address} - ${place.latLng}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun validateUsing_libphonenumber(countryCode: String, phNumber: String) : Boolean{
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val isoCode = phoneNumberUtil.getRegionCodeForCountryCode(countryCode.toInt())
        var phoneNumber: PhoneNumber? = null
        try {
            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode)
        } catch (e: NumberParseException) {
            System.err.println(e)
        }

        val isValid = phoneNumberUtil.isValidNumber(phoneNumber)
        return if (isValid) {
            val internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            Toast.makeText(this, "Phone Number is Valid $internationalFormat", Toast.LENGTH_LONG).show()
            true
        } else {
            Toast.makeText(this, "Phone Number is Invalid $phoneNumber", Toast.LENGTH_LONG).show()
            false
        }

    }

}
package br.com.appzonaazul

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import br.com.appzonaazul.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private  val places = arrayListOf (
        Place("PUC", LatLng(-23.5868031, -46.684306), "Av. Reitor Benedito José Barreto Fonseca, H15 - Parque dos Jacarandás, Campinas - SP", 4.8f),
        Place("PUC", LatLng(-23.5868031, -46.684306), "Av. Reitor Benedito José Barreto Fonseca, H15 - Parque dos Jacarandás, Campinas - SP", 4.8f),
        Place("PUC", LatLng(-23.5868031, -46.684306), "Av. Reitor Benedito José Barreto Fonseca, H15 - Parque dos Jacarandás, Campinas - SP", 4.8f),
        Place("PUC", LatLng(-23.5868031, -46.684306), "Av. Reitor Benedito José Barreto Fonseca, H15 - Parque dos Jacarandás, Campinas - SP", 4.8f),

        )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navegação dos botões da barra de menu
        binding.btnNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.btnNavigationConsultar -> {
                    // Respond to navigation item 1 click
                    abrirTelaConsultarVeiculo()
                    true
                }
                R.id.btnNavigationAreaIrregular -> {
                    // Respond to navigation item 2 click
                    abrirTelaRegistrarIrregularidade()
                    true
                }
                else -> false
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
             mapFragment.getMapAsync { googleMap ->
                googleMap.setInfoWindowAdapter(MarkerInfoAdapter(this))
                addMarkers(googleMap)
                googleMap.setOnMapLoadedCallback {
                 val bounds = LatLngBounds.builder()
                 places.forEach {
                     bounds.include(it.latLng
                     )
                 }

                 googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
             }
        }
    }

    private fun addMarkers(googleMap: GoogleMap) {
            places.forEach { place ->
                var marker =   googleMap.addMarker (
                MarkerOptions()
                    .title(place.name)
                    .snippet(place.adress)
                    .position(place.latLng)
                    .icon(
                        BitmapHelper.vectorToBitMap(this, R.drawable.outline_location_on_black_36dp, ContextCompat.getColor(this, R.color.teal_200))
                    )
            )
                if (marker != null) {
                    marker.tag = places
                }

            }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }

    data class Place (
        val name: String,
        val latLng: LatLng,
        val adress: String,
        val rating: Float
        )

    private fun abrirTelaRegistrarIrregularidade() {
        //navegar para a outra activity
        val intentRegistrarIrregularidade = Intent(this, CameraActivity::class.java)
        startActivity(intentRegistrarIrregularidade)
    }

    private fun abrirTelaConsultarVeiculo() {
        //navegar para a outra activity
        val intentConsultaVeiculo = Intent(this, ConsultaVeiculoActivity::class.java)
        startActivity(intentConsultaVeiculo)

    }

}
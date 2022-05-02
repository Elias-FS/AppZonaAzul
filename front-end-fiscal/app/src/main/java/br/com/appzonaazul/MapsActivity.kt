package br.com.appzonaazul

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import br.com.appzonaazul.api.FireStore
import br.com.appzonaazul.classes.Ticket

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import br.com.appzonaazul.databinding.ActivityMapsBinding
import br.com.appzonaazul.util.RetrofitClient
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val places: MutableList<ZonaAzul> = getZonaAzul()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(places)
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
                     bounds.include(LatLng(
                         it.LatLng[0].toString().toDouble(),
                         it.LatLng[1].toString().toDouble()))
                 }

                 googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
             }
        }
    }
    private fun getZonaAzul():MutableList<ZonaAzul> {
        println("$$$$$$$\\Zona Azul iniciada$$$$$$$")
        val firebase = RetrofitClient.getRetrofitInstance().create(FireStore::class.java)
        val zonaazul = mutableListOf<ZonaAzul>()
        firebase.getZonaAzul().enqueue(object : retrofit2.Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>){
                val data = mutableListOf<JsonElement>()
                response.body()?.iterator()?.forEach {
                    data.add(it)
                }
                println(data.toString())
                for(i in data){
                    zonaazul.add(Gson().fromJson(i, ZonaAzul::class.java))
                }

                println(zonaazul)
            }


            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                println("Error de conexão")
            }
        })
        return zonaazul

    }

    private fun addMarkers(googleMap: GoogleMap) {
        val local1 = LatLng(places[0].LatLng[0].toString().toDouble(),places[0].LatLng[1].toString().toDouble())
        val local2 = LatLng(places[1].LatLng[0].toString().toDouble(),places[1].LatLng[1].toString().toDouble())

        places.forEach { place ->
                val marker =   googleMap.addMarker (
                MarkerOptions()
                    .title(place.title)
                    .snippet(place.snippet)
                    .position(LatLng(
                        place.LatLng[0].toString().toDouble(),
                        place.LatLng[1].toString().toDouble()))
                    .icon(
                        BitmapHelper.vectorToBitMap(this, R.drawable.outline_location_on_black_36dp, ContextCompat.getColor(this, R.color.teal_200))
                    )
            )
                if (marker != null) {
                    marker.tag = places
                }

            }
        googleMap.addPolyline(PolylineOptions().add(local1,local2).width(35f).color(Color.BLUE))


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))



    }

    data class ZonaAzul (
        val LatLng:Array<Number>,
        val title: String,
        val snippet: String
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
package br.com.appzonaazul

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import br.com.appzonaazul.databinding.ActivityConsultaVeiculoBinding
import br.com.appzonaazul.util.RetrofitClient
import br.com.appzonaazul.api.FireStore
import br.com.appzonaazul.classes.Ticket
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response

class ConsultaVeiculoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityConsultaVeiculoBinding
    private lateinit var tvHoraInicio:AppCompatTextView
    private lateinit var btnVerificar:AppCompatButton
    private lateinit var etPlaca:AppCompatEditText
    private lateinit var tvHoraFim:AppCompatTextView
    private lateinit var tvTempoDecorrido:AppCompatTextView
    private lateinit var tvStatus:AppCompatTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflar a activity
        binding = ActivityConsultaVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvHoraFim = findViewById(R.id.tvHoraFim)
        btnVerificar = findViewById(R.id.btnVerificar)

        btnVerificar.setOnClickListener {
            etPlaca = findViewById(R.id.etPlaca)
            hideMyKeyboard()
            consultaVeiculo(etPlaca.text.toString())
        }

        binding.btnRegistrarIrregularidade.visibility = View.INVISIBLE
        binding.btnRegistrarIrregularidade.setOnClickListener {
            //clicar para ir consultar o veiculo
            abrirTelaRegistrarIrregularidade()
        }


        // Navegação dos botões da barra de menu
        binding.btnNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btnNavigationItinerario -> {
                    // Respond to navigation item 1 click
                    openWindowItinerario()
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


    }
    private fun abrirTelaRegistrarIrregularidade() {
        //navegar para a outra activity
        val intentRegistrarIrregularidade = Intent(this, CameraActivity::class.java)
        startActivity(intentRegistrarIrregularidade)

    }



    private fun consultaVeiculo(placa: String) {
        val firebase = RetrofitClient.getRetrofitInstance().create(FireStore::class.java)
        println(firebase.toString())
        firebase.getTickets().enqueue(object : retrofit2.Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>,response: Response<JsonArray>){
                val data = mutableListOf<JsonElement>()
                response.body()?.iterator()?.forEach {
                    data.add(it)
                }
                val tickets = mutableListOf<Ticket>()
                for(i in data){
                    tickets.add(Gson().fromJson(i,Ticket::class.java))
                }
                tvHoraInicio =  findViewById(R.id.tvHoraInicio)
                tvHoraFim = findViewById(R.id.tvHoraFim)
                tvTempoDecorrido = findViewById(R.id.tvTempoDecorrido)
                tvStatus = findViewById(R.id.tvStatus)

                for (it in tickets){
                    if (!(placa in it.placaVeiculo)) {
                        Snackbar.make(tvStatus,"!!!  Placa (${placa}) Não Consta no Sistema !!!",
                            Snackbar.LENGTH_LONG).show()
                    } else {
                        if (it.placaVeiculo==placa){
                            tvHoraInicio.text = it.horaInicio
                            tvHoraFim.text = it.horaFim
                            tvTempoDecorrido.text = it.tempoDecorrido()
                            tvStatus.text = "Regular"
                            binding.btnRegistrarIrregularidade.visibility = View.INVISIBLE
                            if (it.tempoDecorrido().subSequence(0,2).toString().toInt() >= 1)
                                if(it.tempoDecorrido().subSequence(3,5).toString().toInt()>0||
                                    it.tempoDecorrido().subSequence(6,8).toString().toInt()>0) {
                                    tvStatus.text = "Irregular"
                                    binding.btnRegistrarIrregularidade.visibility = View.VISIBLE
                                }
                        }

                    }

                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                println("Error de conexão")
            }
        })

    }
    private fun hideMyKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        } else
            window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun openWindowItinerario() {
        //navegar para a outra activity
        val openItinerario = Intent(this, MapsActivity::class.java)
        startActivity(openItinerario)
    }
}
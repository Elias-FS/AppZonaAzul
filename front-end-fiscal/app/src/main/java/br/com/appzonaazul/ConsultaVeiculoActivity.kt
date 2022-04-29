package br.com.appzonaazul

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.startActivity
import br.com.appzonaazul.databinding.ActivityConsultaVeiculoBinding
import br.com.appzonaazul.util.RetrofitClient
import br.com.appzonaazul.api.FireStore
import br.com.appzonaazul.classes.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import java.util.function.BiConsumer

class ConsultaVeiculoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultaVeiculoBinding
    private lateinit var tvHoraInicio: AppCompatTextView
    private lateinit var btnVerificar: AppCompatButton
    private lateinit var etPlaca: AppCompatEditText
    private lateinit var tvHoraFim: AppCompatTextView
    private lateinit var tvTempoDecorrido: AppCompatTextView
    private lateinit var tvStatus: AppCompatTextView
    private lateinit var functions:FirebaseFunctions
    private lateinit var gson: Gson
    private val logEntry = "TICKET_ENCONTRADO";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflar a activity
        binding = ActivityConsultaVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrarIrregularidade.setOnClickListener {
            //clicar para ir consultar o veiculo
            abrirTelaRegistrarIrregularidade()
        }

        functions = Firebase.functions("southamerica-east1")


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

        tvHoraFim = findViewById(R.id.tvHoraFim)
        tvHoraInicio = findViewById(R.id.tvHoraFim)
        btnVerificar = findViewById(R.id.btnVerificar)
        etPlaca = findViewById(R.id.etPlaca)

        btnVerificar.setOnClickListener {

            if (etPlaca.text.isNullOrEmpty()) {
                Snackbar.make(etPlaca, "Informe a placa", Snackbar.LENGTH_LONG).show()
            } else {

                var placa = etPlaca.text.toString()

                findByPlate(placa).addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Plate: ", etPlaca.text.toString())

                        var Entrada: Timestamp? = null
                        var Saida: Timestamp? = null
                        val result =
                            gson.fromJson(task.result, FunctionsGenericResponse::class.java)
                        Log.i("Status", result.status.toString())
                        Log.i("Message", result.message.toString())

                        val payload =
                            gson.fromJson(
                                result.payload.toString(),
                                PayloadGenericResponse.PayloadGenericResponse::class.java
                            )

                        Log.i("placaVeiculo", payload.placaVeiculo.toString())

                        val horaEntrada =
                            gson.fromJson(
                                payload.horaFim.toString(),
                                TimeGenericResponse::class.java
                            )

                        if (horaEntrada != null) {
                            Entrada =
                                horaEntrada.seconds?.let { it1 ->
                                    horaEntrada.nanoseconds?.let { it2 ->
                                        Timestamp(
                                            it1,
                                            it2
                                        )
                                    }
                                }
                        }

                        val horaSaida =
                            gson.fromJson(
                                payload.horaFim.toString(),
                                TimeGenericResponse::class.java
                            )

                        if (horaSaida != null) {
                            Saida =
                                horaSaida.seconds?.let { it1 ->
                                    horaSaida.nanoseconds?.let { it2 ->
                                        Timestamp(
                                            it1,
                                            it2
                                        )
                                    }
                                }

                            if (Entrada != null && Saida != null) {
                                MaterialAlertDialogBuilder(this)
                                    .setTitle("${result.message.toString()}")
                                    .setMessage(
                                        "Placa: ${payload.placaVeiculo.toString()}\nEntrada: ${
                                            Entrada.toDate().toString()
                                        }\nSaida: ${Saida.toDate().toString()}"
                                    )
                                    .setNeutralButton(
                                        "OK"
                                    ) { dialog, which -> }
                                    .show()
                            }
                        }
                    }
                })

            }

        }
    }




    private fun abrirTelaRegistrarIrregularidade() {
        //navegar para a outra activity
        val intentRegistrarIrregularidade = Intent(this, CameraActivity::class.java)
        startActivity(intentRegistrarIrregularidade)
    }
    private fun openWindowItinerario() {
        //navegar para a outra activity
        val openItinerario = Intent(this, MapsActivity::class.java)
        startActivity(openItinerario)
    }


    private fun hideMyKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        } else
            window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun findByPlate(placa: String): Task<String> {

        val data = hashMapOf(
            "placa" to placa
        )

        Log.i("Placa: ", placa)



        return functions
            .getHttpsCallable("findByPlate")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                Log.i("RES: ", res)
                res
            }
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
                    if (it.placaVeiculo==placa){
                        tvHoraInicio.text = it.horaInicio
                        tvHoraFim.text = it.horaFim
                        tvTempoDecorrido.text = it.tempoDecorrido()
                        if (it.tempoDecorrido().subSequence(0,2).toString().toInt() >= 1)
                            if(it.tempoDecorrido().subSequence(3,5).toString().toInt()>0||it.tempoDecorrido().subSequence(6,8).toString().toInt()>0)
                                    tvStatus.text = "Irregular"

                    }
                }


            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                println("Error de conexão")
            }

        })
    }

}




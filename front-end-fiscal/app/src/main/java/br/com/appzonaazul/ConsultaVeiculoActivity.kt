package br.com.appzonaazul

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
import br.com.appzonaazul.databinding.ActivityConsultaVeiculoBinding
import br.com.appzonaazul.util.RetrofitClient
import br.com.appzonaazul.api.FireStore
import br.com.appzonaazul.classes.FunctionsGenericResponse
import br.com.appzonaazul.classes.PayloadGenericResponse
import br.com.appzonaazul.classes.Ticket
import br.com.appzonaazul.classes.TimeGenericResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDateTime

class ConsultaVeiculoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityConsultaVeiculoBinding
    private lateinit var tvHoraInicio:AppCompatTextView
    private lateinit var btnVerificar:AppCompatButton
    private lateinit var etPlaca: TextInputEditText
    private lateinit var tvHoraFim:AppCompatTextView
    private lateinit var tvTempoDecorrido:AppCompatTextView
    private lateinit var tvStatus:AppCompatTextView
    private lateinit var gson: Gson
    private lateinit var functions: FirebaseFunctions
    private var ticket = Ticket("a", Timestamp(0,0), Timestamp(0,0))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflar a activity
        binding = ActivityConsultaVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvHoraFim = findViewById(R.id.tvHoraFim)
        btnVerificar = findViewById(R.id.btnVerificar)

        gson =  GsonBuilder().enableComplexMapKeySerialization().create()

        functions = Firebase.functions("southamerica-east1")

        etPlaca = findViewById(R.id.etPlaca)

        btnVerificar.setOnClickListener {
            hideMyKeyboard()

            if (binding.etPlaca.text.isNullOrEmpty()) {
                Snackbar.make(binding.etPlaca, "Informe a placa", Snackbar.LENGTH_LONG).show()
            } else {

                validateTicket(etPlaca.text.toString()).addOnCompleteListener { task ->
                    Log.i(String.toString(), etPlaca.toString())

                    Log.i(String.toString(), ticket.horaInicio.toString())

                    val result =
                        gson.fromJson(task.result, FunctionsGenericResponse::class.java)

                    Log.i(String.toString(), result.status.toString())

                    val payload =
                        gson.fromJson(result.payload.toString(), PayloadGenericResponse::class.java)


                    val horaInicio =
                        gson.fromJson(payload.horaInicio.toString(), TimeGenericResponse::class.java)

                    val horaFim =
                        gson.fromJson(payload.horaFim.toString(), TimeGenericResponse::class.java)

                    if(horaInicio!=null && horaFim!=null) {
                        ticket.placaVeiculo = payload.placaVeiculo.toString()
                        ticket.horaInicio =
                            horaInicio.seconds?.let { it1 ->
                                horaInicio.nanoseconds?.let { it2 ->
                                    Timestamp(it1, it2)
                                }
                            }
                        ticket.horaFim =
                            horaFim.seconds?.let { it1 ->
                                horaFim.nanoseconds?.let { it2 ->
                                    Timestamp(it1, it2)
                                }
                            }
                    }

                    if(result.status.toString() == "SUCCESS") {

                        val horasFinais = ticket.horaFim?.toDate()?.hours.toString()
                        val minutosFinais = ticket.horaFim?.toDate()?.minutes.toString()

                        val dataAtual = LocalDateTime.now()
                        val horaAtual = dataAtual.hour

                        val minutosAtual = dataAtual.minute
                        val horaInicio  = ticket.horaInicio?.toDate()?.hours.toString()

                        val minutesInicio = ticket.horaInicio?.toDate()?.minutes.toString()
                        val minutosDecorrido = (horaAtual * 60) + minutosAtual
                        val minutosInicio = ((horaInicio.toInt()  * 60 + minutesInicio.toInt()))
                        val tempoDecorrido = (minutosDecorrido - minutosInicio) / 60


                        val minutosValidacao = (horasFinais.toInt() * 60) + minutosFinais.toInt()

                        Log.i(String.toString(), minutosDecorrido.toString())
                        Log.i(String.toString(), minutosValidacao.toString())

                        tvStatus = findViewById(R.id.tvStatus)

                        if(horasFinais.toInt() >= horaAtual) {
                            tvStatus.text = "Regular"
                            binding.btnRegistrarIrregularidade.visibility = View.INVISIBLE
                        } else{
                            binding.btnRegistrarIrregularidade.visibility = View.VISIBLE
                            tvStatus.text = "Irregular"
                        }

                        tvHoraInicio =  findViewById(R.id.tvHoraInicio)
                        tvHoraInicio.text = ticket.horaInicio?.toDate()?.hours.toString() + ":" + ticket.horaInicio?.toDate()?.minutes.toString()
                        tvHoraFim.text = ticket.horaFim?.toDate()?.hours.toString() + ":" + ticket.horaFim?.toDate()?.minutes.toString()

                    }

                    //1 - dataAtual - dataInicio = tempoDecorrido if(tempoDecorrido < 0 )  ok else não ok
                    //dataAtual = 15 dataInicio = 14
                    //2 - dataAtual - dataFim = tempoTotal
                    //3 - if tempoDecorrido > tempoTotal = irregular

                    //---------------------------------------------------------------------

                    //1 if dataFim > dataAtual {regular} else {irregular}

                }

            }


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
                tvStatus = findViewById(R.id.tvStatus)

                for (it in tickets){
                    if (!(placa in it.placaVeiculo)) {
                        Snackbar.make(tvStatus,"!!!  Placa (${placa}) Não Consta no Sistema !!!",
                            Snackbar.LENGTH_LONG).show()
                    } else {
                        if (it.placaVeiculo==placa){
                            tvHoraInicio.text = it.horaInicio.toString()
                            tvHoraFim.text = it.horaFim.toString()
                            //tvTempoDecorrido.text = it.tempoDecorrido()
                            tvStatus.text = "Regular"
                            binding.btnRegistrarIrregularidade.visibility = View.INVISIBLE
                            /*if (it.tempoDecorrido().subSequence(0,2).toString().toInt() >= 1)
                                if(it.tempoDecorrido().subSequence(3,5).toString().toInt()>0||
                                    it.tempoDecorrido().subSequence(6,8).toString().toInt()>0) {
                                    tvStatus.text = "Irregular"
                                    binding.btnRegistrarIrregularidade.visibility = View.VISIBLE
                                }*/
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

    private fun validateTicket(placa: String): Task<String> {

        val data = hashMapOf(
            "placaVeiculo" to placa
        )
        Log.i(String.toString(), data.toString())

        return functions
            .getHttpsCallable("findByPlate")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                Log.i(String.toString(), res.toString())
                res
            }
    }



}
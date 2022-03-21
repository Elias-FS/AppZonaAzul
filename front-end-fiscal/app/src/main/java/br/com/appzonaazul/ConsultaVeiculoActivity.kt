package br.com.appzonaazul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import br.com.appzonaazul.databinding.ActivityConsultaVeiculoBinding
import br.com.appzonaazul.util.RetrofitClient
import br.com.appzonaazul.api.FireStore
import br.com.appzonaazul.classes.Ticket
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

class ConsultaVeiculoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityConsultaVeiculoBinding
    private lateinit var tvHoraChegada:AppCompatTextView
    private lateinit var btnVerificar:AppCompatButton
    private lateinit var etPlaca:AppCompatEditText
    private lateinit var tvHoraSaida:AppCompatTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflar a activity
        binding = ActivityConsultaVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrarIrregularidade.setOnClickListener {
            //clicar para ir consultar o veiculo
            abrirTelaRegistrarIrregularidade()

        }
        tvHoraSaida = findViewById(R.id.tvHoraSaida)
        btnVerificar = findViewById(R.id.btnVerificar)
        btnVerificar.setOnClickListener {
            tvHoraSaida.text = "here"
            etPlaca = findViewById(R.id.etPlaca)
            consultaVeiculo(etPlaca.text.toString())
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
        firebase.getTickets().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>,response: Response<JsonObject>){
                val data = mutableListOf<String>()
                response.body()?.keySet()?.iterator()?.forEach {
                    data.add(it)
                }
                println("here")
                val tickets : MutableList<Ticket> = mutableListOf<Ticket>()
                for(i in data){
                   tickets.add(Gson().fromJson(i,Ticket::class.java))
                }
                println("here")
                tvHoraChegada =  findViewById(R.id.tvHoraChegada)
                for (it in tickets){
                    println(it)
                    if (it.placaVeiculo==placa){
                        tvHoraChegada.text = it.placaVeiculo.toString()
                    }
                }


            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                println("Error de conexão")
            }
        })
    }
}
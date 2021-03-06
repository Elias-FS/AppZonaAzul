package br.com.appzonaazul

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.appzonaazul.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflar a activity
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenConsultarVeiculo.setOnClickListener {
            //clicar para ir consultar o veiculo
            abrirTelaConsultarVeiculo()
        }

        binding.btnOpenItinerario.setOnClickListener {
            openWindowItinerario()
        }

    }
    private fun abrirTelaConsultarVeiculo() {
        //navegar para a outra activity
        val intentConsultaVeiculo = Intent(this, ConsultaVeiculoActivity::class.java)
        startActivity(intentConsultaVeiculo)

    }

    private fun openWindowItinerario() {
        //navegar para a outra activity
        val openItinerario = Intent(this, MapsActivity::class.java)
        startActivity(openItinerario)

    }
}


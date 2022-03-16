package br.com.appzonaazul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.appzonaazul.databinding.ActivityConsultaVeiculoBinding

class ConsultaVeiculoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityConsultaVeiculoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflar a activity
        binding = ActivityConsultaVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrarIrregularidade.setOnClickListener {
            //clicar para ir consultar o veiculo
            abrirTelaRegistrarIrregularidade()

        }

    }
    private fun abrirTelaRegistrarIrregularidade() {
        //navegar para a outra activity
        val intentRegistrarIrregularidade = Intent(this, CameraActivity::class.java)
        startActivity(intentRegistrarIrregularidade)

    }
}
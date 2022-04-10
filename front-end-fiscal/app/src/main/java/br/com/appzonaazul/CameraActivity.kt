package br.com.appzonaazul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import br.com.appzonaazul.databinding.ActivityCameraBinding
import com.google.android.material.snackbar.Snackbar

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflar a activity
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navegação dos botões da barra de menu
        binding.btnNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.btnNavigationItinerario -> {
                    // Respond to navigation item 1 click
                    openWindowItinerario()
                    true
                }
                R.id.btnNavigationConsultar -> {
                    // Respond to navigation item 2 click
                    abrirTelaConsultarVeiculo()
                    true
                }
                else -> false
            }
        }


        binding.btnOpenCamera.setOnClickListener {
            //solicitar permissão da CAMERA
            cameraProviderResult.launch(android.Manifest.permission.CAMERA)
        }
    }


        private val cameraProviderResult =
            registerForActivityResult(ActivityResultContracts.RequestPermission()){
                if(it){
                    abrirTelaPreview()
                }else{
                    Snackbar.make(binding.root, "Você não concedeu permissões para usar a câmera.", Snackbar.LENGTH_INDEFINITE).show()
                }
            }

        private fun abrirTelaPreview() {
            //navegar para a outra activity
            val intentCameraPreview = Intent(this, CameraPreviewActivity::class.java)
            startActivity(intentCameraPreview)
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
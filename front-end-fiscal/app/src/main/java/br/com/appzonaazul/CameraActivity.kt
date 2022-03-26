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


}
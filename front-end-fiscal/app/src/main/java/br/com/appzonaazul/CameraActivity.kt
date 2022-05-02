package br.com.appzonaazul

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.*
import androidx.core.content.FileProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.time.LocalDateTime


private const val REQUEST_CODE = 42

class CameraActivity : AppCompatActivity() {

    private var FILE_NAME = "photo.jpg"
    private lateinit var tvData: AppCompatTextView
    private lateinit var btnEnviarFoto : AppCompatButton
    private lateinit var ivFoto : AppCompatImageButton
    private lateinit var ivFoto2 : AppCompatImageButton
    private lateinit var ivFoto3 : AppCompatImageButton
    private lateinit var ivFoto4 : AppCompatImageButton
    private lateinit var btnNavigation: BottomNavigationView
    private lateinit var photoFile: File
    private var paths: MutableList<String> = mutableListOf("","","","")
    private lateinit var photo: File
    val storage = Firebase.storage("gs://backendfiscal")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


        tvData  = findViewById(R.id.tvData)
        ivFoto = findViewById(R.id.ivFoto)
        ivFoto2 = findViewById(R.id.ivFoto2)
        ivFoto3 = findViewById(R.id.ivFoto3)
        ivFoto4 = findViewById(R.id.ivFoto4)
        btnEnviarFoto = findViewById(R.id.btnEnviarFoto)
        btnNavigation = findViewById(R.id.btnNavigation)

        ivFoto.setOnClickListener(){
            pegarFoto(1)
        }
        ivFoto2.setOnClickListener(){
            pegarFoto(2)
        }
        ivFoto3.setOnClickListener(){
            pegarFoto(3)
        }
        ivFoto4.setOnClickListener(){
            pegarFoto(4)
        }
        btnEnviarFoto.setOnClickListener(){
            if(paths[0].isEmpty()||paths[1].isEmpty()||paths[2].isEmpty()||paths[3].isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "!!!ERRO!!!Por favor, tire as quatro fotos antes de enviar!",
                    Toast.LENGTH_SHORT
                ).show()
                pegarData()
            } else {
                // Create a storage reference from our app
                val storageRef = storage.reference
                for (i in paths) {
                    var file = Uri.fromFile(File("${i}"))
                    val photoRef = storageRef.child("br.com.appzonaazul/${file.lastPathSegment}")
                    photoRef.putFile(file)
            }

                Snackbar.make(btnEnviarFoto, "Imagem registrada com sucesso", Snackbar.LENGTH_LONG).show()
                val intent = Intent(this, CameraActivity::class.java)
                startActivity(intent)
        }

        }
        // Navegação dos botões da barra de menu
        btnNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btnNavigationConsultar -> {
                    // Respond to navigation item 1 click
                    abrirTelaConsultarVeiculo()
                    true
                }
                R.id.btnNavigationItinerario -> {
                    // Respond to navigation item 2 click
                    openWindowItinerario()
                    true
                }
                else -> false
            }
        }
   }

    private fun pegarFoto(REQUEST_CODE: Int){
        val tirarFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)

        //tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
        val fileProvider = FileProvider.getUriForFile(this, "br.com.appzonaazul.fileprovider", photoFile)
        tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (tirarFoto.resolveActivity(this.packageManager) != null) {
            startActivityForResult(tirarFoto, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Erro ao abrir a camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhotoFile(fileName: String): File {
        // Use "getExternalFilesDir" on Context to access package-especific directories
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            val img: Drawable = BitmapDrawable(resources,bitmap)
            if(requestCode == 1) {
                ivFoto.background = img
                ivFoto.setImageIcon(null)
                paths[0] = photoFile.absolutePath
                Log.i("ENDERECO",paths[0])
            }
            if(requestCode == 2) {
                ivFoto2.background = img
                ivFoto2.setImageIcon(null)
                paths[1] = photoFile.absolutePath
                Log.i("ENDERECO",paths[1])
            }
            if(requestCode == 3) {
                ivFoto3.background = img
                ivFoto3.setImageIcon(null)
                paths[2] = photoFile.absolutePath
                Log.i("ENDERECO",paths[2])
            }
            if(requestCode == 4) {
                ivFoto4.background = img
                ivFoto4.setImageIcon(null)
                paths[3] = photoFile.absolutePath
                Log.i("ENDERECO",paths[3])
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun pegarData(){
        val data = LocalDateTime.now()
        tvData.text = data.toString()
    }

    private fun openWindowItinerario() {
        //navegar para a outra activity
        val openItinerario = Intent(this, MapsActivity::class.java)
        startActivity(openItinerario)
    }

    private fun abrirTelaConsultarVeiculo() {
        //navegar para a outra activity
        val intentConsultaVeiculo = Intent(this, ConsultaVeiculoActivity::class.java)
        startActivity(intentConsultaVeiculo)

    }


}
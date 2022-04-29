package br.com.appzonaazul

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import java.io.File
import java.time.LocalDateTime

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File
class CameraActivity : AppCompatActivity() {

    private lateinit var tvData: AppCompatTextView
    private lateinit var btnTirarFoto : AppCompatButton
    private lateinit var ivFoto : AppCompatImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


        tvData  = findViewById(R.id.tvData)
        ivFoto = findViewById(R.id.ivFoto)
        btnTirarFoto = findViewById(R.id.btnTirarFoto)


        btnTirarFoto.setOnClickListener {
            val tirarFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)
            pegarData()

            //tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            val fileProvider = FileProvider.getUriForFile(this, "br.com.appzonaazul.fileprovider", photoFile)
            tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (tirarFoto.resolveActivity(this.packageManager) != null) {
                startActivityForResult(tirarFoto, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPhotoFile(fileName: String): File {
        // Use "getExternalFilesDir" on Context to access package-especific directories

        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //val pegarfoto = data?.extras?.get("data") as Bitmap
            val pegarfoto = BitmapFactory.decodeFile(photoFile.absolutePath)
            ivFoto.setImageBitmap(pegarfoto)
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun pegarData(){
        val data = LocalDateTime.now()
        tvData.text = data.toString()
    }


}
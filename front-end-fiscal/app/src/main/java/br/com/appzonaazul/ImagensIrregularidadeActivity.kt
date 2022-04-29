package br.com.appzonaazul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ImagensIrregularidadeActivity : AppCompatActivity() {

    lateinit var iv_img4 : ImageView
    lateinit var iv_img3 : ImageView
    lateinit var iv_img2 : ImageView
    lateinit var iv_img1 : ImageView

    lateinit var cameraActivity: CameraActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagens_irregularidade)

        iv_img4 = findViewById(R.id.iv_img4)
        iv_img3 = findViewById(R.id.iv_img3)
        iv_img2 = findViewById(R.id.iv_img2)
        iv_img1 = findViewById(R.id.iv_img1)


        cameraActivity
    }
}
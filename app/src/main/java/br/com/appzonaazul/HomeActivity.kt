package br.com.appzonaazul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.appzonaazul.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflar a activity
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
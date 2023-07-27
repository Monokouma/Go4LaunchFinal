package com.despaircorp.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.despaircorp.atoms.coworker.CoworkerRowViewState
import com.despaircorp.demo.databinding.DemoActivityBinding

class DemoActivity : AppCompatActivity() {

    private val binding by viewBinding { DemoActivityBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.demoCoworkerRow.bind(
            CoworkerRowViewState(
                imageUrl = "https://i.imgur.com/auuMHcn.png",
                sentence = "Nino va bouffer au McDo comme d'habitude",
                onClick = {
                    Toast.makeText(this@DemoActivity, "Redirection vers le chat", Toast.LENGTH_LONG).show()
                }
            )
        )
    }
}
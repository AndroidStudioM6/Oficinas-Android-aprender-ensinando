package com.example.oficina_android

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_about)

        // Ajusta a tela para não ficar atrás das barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.mainAbout)
        ) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Botão de voltar na barra superior
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarAbout)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Botão de voltar no final da tela
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }
    }
}

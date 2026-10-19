package com.example.oficina_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar

/**
 * Fragment para a tela Sobre.
 */
class AboutFragment : Fragment(R.layout.activity_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarAbout)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val btnBack = view.findViewById<Button>(R.id.btnBack)
        btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}

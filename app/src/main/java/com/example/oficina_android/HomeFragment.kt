package com.example.oficina_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

/**
 * Fragment para a tela inicial (Home).
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnOpenTaskList)?.setOnClickListener {
            (activity as? MainActivity)?.selectPage(2)
        }
    }
}

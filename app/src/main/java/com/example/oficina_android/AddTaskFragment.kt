package com.example.oficina_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText

class AddTaskFragment : Fragment(R.layout.fragment_add_task) {

    private lateinit var viewModel: TaskViewModel
    private lateinit var etTaskInput: TextInputEditText
    private lateinit var btnAdd: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        etTaskInput = view.findViewById(R.id.etTaskInput)
        btnAdd = view.findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val text = etTaskInput.text?.toString()?.trim() ?: ""
            if (text.isNotEmpty()) {
                viewModel.addTask(text)
                etTaskInput.text?.clear()
                Toast.makeText(requireContext(), "Tarefa adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                (activity as? MainActivity)?.selectPage(2)
            } else {
                Toast.makeText(requireContext(), "Digite o título da tarefa", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

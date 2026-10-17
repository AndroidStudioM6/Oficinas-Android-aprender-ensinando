package com.example.oficina_android

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class TaskCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_create)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainTaskCreate)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarTaskCreate)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val priorityOptions = arrayOf(
            getString(R.string.priority_high),
            getString(R.string.priority_medium),
            getString(R.string.priority_low)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, priorityOptions)
        val actvPriority = findViewById<AutoCompleteTextView>(R.id.actvPriority)
        actvPriority.setAdapter(adapter)

        val btnSaveTask = findViewById<Button>(R.id.btnSaveTask)
        val btnCancelTask = findViewById<Button>(R.id.btnCancelTask)

        btnSaveTask.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_task_created), Toast.LENGTH_SHORT).show()
            finish()
        }

        btnCancelTask.setOnClickListener {
            finish()
        }
    }
}

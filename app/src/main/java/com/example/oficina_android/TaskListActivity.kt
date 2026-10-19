package com.example.oficina_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class TaskListActivity : AppCompatActivity() {

    // Lista inicial de tarefas
    private val taskList = mutableListOf(
        Task(
            title = "Estudar para a prova de Android",
            description = "Revisar Activity, Layouts XML, Eventos e Material Design 3.",
            priority = "Alta",
            creationDate = "15/10/2026",
            dueDate = "20/10/2026",
            isCompleted = false
        ),
        Task(
            title = "Implementar interface de cadastro de tarefas",
            description = "Criar os campos de título, descrição, prioridade, prazos e checkbox.",
            priority = "Alta",
            creationDate = "16/10/2026",
            dueDate = "22/10/2026",
            isCompleted = false
        ),
        Task(
            title = "Criar tela Sobre o Projeto",
            description = "Apresentar descrição do app, funcionalidades e equipe.",
            priority = "Média",
            creationDate = "17/10/2026",
            dueDate = "25/10/2026",
            isCompleted = true
        )
    )

    private lateinit var adapter: TaskAdapter
    private lateinit var fabAddTask: ExtendedFloatingActionButton
    private lateinit var etTaskInput: TextInputEditText
    private lateinit var btnAdd: Button
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        val rootLayout = findViewById<View>(R.id.mainTaskList)

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        initViews()
        setupToolbar()
        setupRecyclerView()
        setupListeners()
        updateEmptyState()

        // Foca no campo de cadastro quando solicitado
        if (intent.getBooleanExtra("FOCUS_ADD", false)) {
            etTaskInput.requestFocus()
        }
    }

    // Inicializa os componentes da tela
    private fun initViews() {
        toolbar = findViewById(R.id.toolbarTaskList)
        fabAddTask = findViewById(R.id.fabAddTask)
        etTaskInput = findViewById(R.id.etTaskInput)
        btnAdd = findViewById(R.id.btnAdd)
        rvTasks = findViewById(R.id.rvTasks)
        tvEmpty = findViewById(R.id.tvEmpty)
    }

    // Configura a barra superior
    private fun setupToolbar() {
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    // Configura a lista de tarefas
    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            taskList,
            onTaskChecked = { _, _ -> },
            onTaskDeleted = { task ->
                adapter.removeTask(task)
                updateEmptyState()
            }
        )

        rvTasks.adapter = adapter
    }

    // Configura os botões da tela
    private fun setupListeners() {

        // Cadastro rápido de tarefas
        btnAdd.setOnClickListener {
            val text = etTaskInput.text?.toString()?.trim() ?: ""

            if (text.isNotEmpty()) {
                val newTask = Task(title = text)

                adapter.addTask(newTask)
                etTaskInput.text?.clear()
                updateEmptyState()

            } else {
                Toast.makeText(
                    this,
                    "Digite o título da tarefa",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Abre a tela de cadastro completo
        fabAddTask.setOnClickListener {
            val intent = Intent(this, TaskCreateActivity::class.java)
            startActivity(intent)
        }
    }

    // Atualiza a mensagem de lista vazia
    private fun updateEmptyState() {
        if (taskList.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
        }
    }
}

package com.example.oficina_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText

class TaskListActivity : AppCompatActivity() {

    private val taskList = mutableListOf(
        Task(title = "Estudar para a prova"),
        Task(title = "Fazer os exercícios de Android"),
        Task(title = "Comprar pão", isCompleted = true)
    )

    private lateinit var adapter: TaskAdapter
    private lateinit var etTaskInput: TextInputEditText
    private lateinit var btnAdd: Button
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupDrawer(R.id.nav_task_list)
        initViews()
        setupRecyclerView()
        setupListeners()
        updateEmptyState()

        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                finish()
            }
        }

        if (intent.getBooleanExtra("FOCUS_ADD", false)) {
            etTaskInput.requestFocus()
        }
    }

    private fun setupDrawer(selectedId: Int) {
        drawerLayout = findViewById(R.id.drawerLayout)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView = findViewById<NavigationView>(R.id.navView)
        navView.setCheckedItem(selectedId)
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }
                R.id.nav_add_task -> {
                    etTaskInput.requestFocus()
                }
                R.id.nav_task_list -> {
                    // Already here
                }
                R.id.nav_about -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun initViews() {
        etTaskInput = findViewById(R.id.etTaskInput)
        btnAdd = findViewById(R.id.btnAdd)
        rvTasks = findViewById(R.id.rvTasks)
        tvEmpty = findViewById(R.id.tvEmpty)
    }

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

    private fun setupListeners() {
        btnAdd.setOnClickListener {
            val text = etTaskInput.text?.toString()?.trim() ?: ""
            if (text.isNotEmpty()) {
                val newTask = Task(title = text)
                adapter.addTask(newTask)
                etTaskInput.text?.clear()
                updateEmptyState()
            } else {
                Toast.makeText(this, "Digite o título da tarefa", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

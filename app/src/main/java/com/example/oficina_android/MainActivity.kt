package com.example.oficina_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNav: BottomNavigationView

    private var isNavigatingFromUser = true

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(
            getSharedPreferences(SettingsFragment.PREFERENCES, MODE_PRIVATE)
                .getInt(SettingsFragment.KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        TarefaRepositorio.inicializar(applicationContext)

        appBarLayout = findViewById(R.id.appBarLayout)
        topAppBar = findViewById(R.id.topAppBar)
        viewPager = findViewById(R.id.viewPager)
        bottomNav = findViewById(R.id.bottomNavigation)

        // Adjust system bar insets for top app bar padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRoot)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            appBarLayout.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        // ViewPager2 adapter
        viewPager.adapter = MainPagerAdapter(this)

        // Sync ViewPager2 swipe -> BottomNav + Title
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val targetItemId = when (position) {
                    0 -> R.id.nav_home
                    1 -> R.id.nav_add_task
                    2 -> R.id.nav_task_list
                    3 -> R.id.nav_about
                    4 -> R.id.nav_settings
                    else -> R.id.nav_home
                }

                if (bottomNav.selectedItemId != targetItemId) {
                    isNavigatingFromUser = false
                    bottomNav.selectedItemId = targetItemId
                    isNavigatingFromUser = true
                }
                updateTitleForItemId(targetItemId)
            }
        })

        // Sync BottomNav click -> ViewPager2
        bottomNav.setOnItemSelectedListener { item ->
            if (!isNavigatingFromUser) return@setOnItemSelectedListener true
            if (item.itemId == R.id.nav_add_task) {
                handleNavigationItemSelect(item.itemId)
                return@setOnItemSelectedListener true
            }
            handleNavigationItemSelect(item.itemId)
            true
        }

    }

    private fun handleNavigationItemSelect(itemId: Int) {
        val position = getPositionForItemId(itemId)
        if (position != -1) {
            if (viewPager.currentItem != position) {
                viewPager.setCurrentItem(position, true)
            }

            if (bottomNav.selectedItemId != itemId) {
                isNavigatingFromUser = false
                bottomNav.selectedItemId = itemId
                isNavigatingFromUser = true
            }

            updateTitleForItemId(itemId)

        }
    }

    fun selectPage(position: Int) {
        val itemId = when (position) {
            0 -> R.id.nav_home
            1 -> R.id.nav_add_task
            2 -> R.id.nav_task_list
            3 -> R.id.nav_about
            4 -> R.id.nav_settings
            else -> R.id.nav_home
        }
        handleNavigationItemSelect(itemId)
    }

    private fun getPositionForItemId(itemId: Int): Int {
        return when (itemId) {
            R.id.nav_home -> 0
            R.id.nav_add_task -> 1
            R.id.nav_task_list -> 2
            R.id.nav_about -> 3
            R.id.nav_settings -> 4
            else -> -1
        }
    }

    private fun updateTitleForItemId(itemId: Int) {
        topAppBar.title = when (itemId) {
            R.id.nav_home -> getString(R.string.app_name)
            R.id.nav_add_task -> getString(R.string.nav_add_task)
            R.id.nav_task_list -> getString(R.string.nav_task_list)
            R.id.nav_about -> getString(R.string.nav_about)
            R.id.nav_settings -> getString(R.string.nav_settings)
            else -> getString(R.string.app_name)
        }
    }

    private class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 5

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> AddTaskFragment()
                2 -> TaskListFragment()
                3 -> AboutFragment()
                4 -> SettingsFragment()
                else -> HomeFragment()
            }
        }
    }
}

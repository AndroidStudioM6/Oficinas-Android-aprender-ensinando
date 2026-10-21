package com.example.oficina_android

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navigationView: NavigationView

    private var isNavigatingFromUser = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBarLayout)
        topAppBar = findViewById(R.id.topAppBar)
        viewPager = findViewById(R.id.viewPager)
        bottomNav = findViewById(R.id.bottomNavigation)
        navigationView = findViewById(R.id.navigationView)

        // Adjust system bar insets for top app bar padding
        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            appBarLayout.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        // Drawer toggle
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            topAppBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // ViewPager2 adapter
        viewPager.adapter = MainPagerAdapter(this)

        // Sync ViewPager2 swipe -> BottomNav + Drawer + Title
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val currentSelectedId = bottomNav.selectedItemId
                val targetItemId = when (position) {
                    0 -> R.id.nav_home
                    1 -> {
                        if (currentSelectedId == R.id.nav_add_task || currentSelectedId == R.id.nav_task_list) {
                            currentSelectedId
                        } else {
                            R.id.nav_task_list
                        }
                    }
                    2 -> R.id.nav_about
                    else -> R.id.nav_home
                }

                if (bottomNav.selectedItemId != targetItemId) {
                    isNavigatingFromUser = false
                    bottomNav.selectedItemId = targetItemId
                    isNavigatingFromUser = true
                }
                navigationView.setCheckedItem(targetItemId)
                updateTitleForItemId(targetItemId)
            }
        })

        // Sync BottomNav click -> ViewPager2
        bottomNav.setOnItemSelectedListener { item ->
            if (!isNavigatingFromUser) return@setOnItemSelectedListener true
            handleNavigationItemSelect(item.itemId)
            true
        }

        // Sync Drawer Item click -> ViewPager2
        navigationView.setNavigationItemSelectedListener { item ->
            handleNavigationItemSelect(item.itemId)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Back press handling for drawer
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
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

            navigationView.setCheckedItem(itemId)
            updateTitleForItemId(itemId)

            if (itemId == R.id.nav_add_task) {
                supportFragmentManager.setFragmentResult(
                    "TASK_LIST_FOCUS",
                    Bundle().apply { putBoolean("FOCUS_INPUT", true) }
                )
            } else if (itemId == R.id.nav_task_list) {
                supportFragmentManager.setFragmentResult(
                    "TASK_LIST_FOCUS",
                    Bundle().apply { putBoolean("FOCUS_INPUT", false) }
                )
            }
        }
    }

    fun selectPage(position: Int) {
        val itemId = when (position) {
            0 -> R.id.nav_home
            1 -> R.id.nav_task_list
            2 -> R.id.nav_task_list
            3 -> R.id.nav_about
            else -> R.id.nav_home
        }
        handleNavigationItemSelect(itemId)
    }

    private fun getPositionForItemId(itemId: Int): Int {
        return when (itemId) {
            R.id.nav_home -> 0
            R.id.nav_add_task -> 1
            R.id.nav_task_list -> 1
            R.id.nav_about -> 2
            else -> -1
        }
    }

    private fun updateTitleForItemId(itemId: Int) {
        topAppBar.title = when (itemId) {
            R.id.nav_home -> getString(R.string.app_name)
            R.id.nav_add_task -> getString(R.string.nav_add_task)
            R.id.nav_task_list -> getString(R.string.nav_task_list)
            R.id.nav_about -> getString(R.string.nav_about)
            else -> getString(R.string.app_name)
        }
    }

    private class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> TaskListFragment()
                2 -> AboutFragment()
                else -> HomeFragment()
            }
        }
    }
}

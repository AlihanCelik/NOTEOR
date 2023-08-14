package com.example.notesapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var viewPagerAdapter: VpAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        val backgroundWhite=resources.getColor(R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav.background=null
        bottomNav.menu.getItem(1).isEnabled = false
        viewPager = findViewById(R.id.viewpager)
        bottomNavigation = findViewById(R.id.bottomNav)
        viewPagerAdapter = VpAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = backgroundWhite
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = backgroundWhite
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        }

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_add -> {
                    Toast.makeText(this, "Add Note Clicked", Toast.LENGTH_SHORT).show()
                    it.isChecked = true
                    true
                }
                else -> false
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            val index = when (menuItem.itemId) {
                R.id.home -> 0
                R.id.calendar -> 1

                else -> 0
            }
            viewPager.currentItem = index
            true
        }

        // ViewPager'ı dinlemek için Listener ekleyin
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigation.menu.getItem(position).isChecked = true
                when (position) {
                    0 -> bottomNavigation.menu.findItem(R.id.home).isChecked = true
                    1 -> bottomNavigation.menu.findItem(R.id.calendar).isChecked = true
                }
            }
        })

        // ViewPager'a fragment ekleyin
        viewPagerAdapter.addFragment(NoteFragment(), "Home")
        viewPagerAdapter.addFragment(CalendarFragment(), "Calender")

        // İlk olarak seçili fragment'i belirtin
        viewPager.currentItem = 0


    }
    fun CreateNoteButton(view : View){
        val intent= Intent(this,CreateNoteActivity::class.java)
        startActivity(intent)

    }
    fun onMenuBarClick(view: View?) {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.openDrawer(GravityCompat.START)
    }



}
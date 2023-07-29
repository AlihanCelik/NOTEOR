package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var viewPagerAdapter: VpAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewpager)
        bottomNavigation = findViewById(R.id.bottomNav)
        viewPagerAdapter = VpAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter

        // BottomNavigationView'daki öğeleri dinlemek için Listener ekleyin
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
            }
        })

        // ViewPager'a fragment ekleyin
        viewPagerAdapter.addFragment(NoteFragment(), "Home")
        viewPagerAdapter.addFragment(CalendarFragment(), "Calender")

        // İlk olarak seçili fragment'i belirtin
        viewPager.currentItem = 0


    }



}
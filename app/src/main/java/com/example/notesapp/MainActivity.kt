package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var viewPagerAdapter: VpAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav.background=null
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



}
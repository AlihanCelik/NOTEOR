package com.example.notesapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.notesapp.Adapter.VpAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val CREATE_NOTE_REQUEST = 1
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var viewPagerAdapter: VpAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        val backgroundWhite = resources.getColor(R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav.background = null
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

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        findViewById<NavigationView>(R.id.nav_view).bringToFront()

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setCheckedItem(R.id.nav_note)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_add -> {
                    val intent=Intent(applicationContext,CreateNoteActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_note -> {
                    viewPager.currentItem = 0
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true

                }
                R.id.nav_trash -> {
                    val intent=Intent(applicationContext,TrashActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_favorites -> {
                    val intent=Intent(applicationContext,FavoritesActivtity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_calendar -> {
                    viewPager.currentItem = 1
                    drawerLayout.closeDrawer(GravityCompat.START)
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
                    0 -> {
                        bottomNavigation.menu.findItem(R.id.home).isChecked = true
                        navigationView.setCheckedItem(R.id.nav_note)
                    }
                    1 -> {bottomNavigation.menu.findItem(R.id.calendar).isChecked = true
                        navigationView.setCheckedItem(R.id.nav_calendar)
                    }
                }
            }
        })

        // ViewPager'a fragment ekleyin
        viewPagerAdapter.addFragment(NoteFragment.newInstance(), "Home")
        viewPagerAdapter.addFragment(CalendarFragment.newInstance(), "Calender")

        // İlk olarak seçili fragment'i belirtin
        viewPager.currentItem = 0


    }

    fun CreateNoteButton(view: View) {
        val intent = Intent(this, CreateNoteActivity::class.java)
        startActivityForResult(intent, CREATE_NOTE_REQUEST)

    }

    fun onMenuBarClick(view: View?) {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val noteFragment = viewPagerAdapter.fragmentList[0] as? NoteFragment
            noteFragment?.updateRecyclerView()
            val calendarFragment=viewPagerAdapter.fragmentList[1]as? CalendarFragment
            calendarFragment?.updateRecyclerView()
        }
    }





}
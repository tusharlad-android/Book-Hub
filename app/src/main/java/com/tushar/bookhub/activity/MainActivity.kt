package com.tushar.bookhub.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.tushar.bookhub.R
import com.tushar.bookhub.fragment.AboutFragment
import com.tushar.bookhub.fragment.DashboardFragment
import com.tushar.bookhub.fragment.FavouitesFragment
import com.tushar.bookhub.fragment.ProfileFragment


class MainActivity : AppCompatActivity() {
    lateinit var actionBarDrawerToggle:ActionBarDrawerToggle
    lateinit var drawerLayout :DrawerLayout
    lateinit var coordinatorLayout :CoordinatorLayout
    lateinit var toolbar : Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView : NavigationView
     var previousMenuItem:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)
        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frame)
        navigationView=findViewById(R.id.navigationView)
        setUpToolbar()
        openDashboard()

         actionBarDrawerToggle= ActionBarDrawerToggle(this@MainActivity,drawerLayout,
             R.string.open_drawer,
             R.string.close_drawer
         )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.itmDashboard ->{
                   openDashboard()
                    drawerLayout.closeDrawers()
                    navigationView.setCheckedItem(R.id.itmDashboard)


                }
                R.id.itmFavourites ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        FavouitesFragment()
                    ).commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                    navigationView.setCheckedItem(R.id.itmFavourites)
                }
                R.id.itmProfile ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment()).commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                    navigationView.setCheckedItem(R.id.itmProfile)
                }
                R.id.itmaboutapp ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame, AboutFragment()).commit()
                    supportActionBar?.title="About"
                    drawerLayout.closeDrawers()
                    navigationView.setCheckedItem(R.id.itmaboutapp)
                }


            }
            return@setNavigationItemSelectedListener true


        }
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    fun openDashboard(){
        supportFragmentManager.beginTransaction().replace(R.id.frame, DashboardFragment()).commit()
        supportActionBar?.title="Dashboard"

        navigationView.setCheckedItem(R.id.itmDashboard)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }

    }

}
package com.example.logggg

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.logggg.databinding.ActivityBase2Binding
import com.example.logggg.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class Base_Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding:ActivityBase2Binding
    private var userEmail: String? = null
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBase2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolbar,R.string.nav_open,R.string.nav_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        binding.bottomNavigation.background= null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.bottom_home -> {
                    openFragment(Sales(), "Sales Fragment Opened")
                }

                R.id.bottom_Outlets -> { // Handle Outlets button click
                    openFragment(OutletsFragment(), "Outlets Fragment Opened")
                }
                R.id.bottom_Menu -> {
                    openFragment(RoutesFragment(), "Routes Fragment Opened")
                }
                R.id.bottom_cart -> {
                    openFragment(SalesOrderFragment(), "Routes Fragment Opened")
                }


            }
            true
        }

        // Set HomeFragment as default
        if (savedInstanceState == null) {
            openFragment(Sales(), "Sales Fragment Opened")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_fresh -> openFragment(Sales(),"Home Fragment Opened")
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun retrieveUserDetails() {
        userEmail = intent.getStringExtra("USER_EMAIL")
        userName = intent.getStringExtra("USER_NAME")
    }



    private fun openFragment(fragment: Fragment, toastMessage: String) {
        // Set arguments for the fragment
        val bundle = Bundle().apply {
            putString("USER_EMAIL", userEmail)
            putString("USER_NAME", userName)
        }
        fragment.arguments = bundle

        // Begin fragment transaction
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null) // Add to back stack for navigation
        fragmentTransaction.commit()

        // Display the toast message
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
    }
    // To navigate to OutletDetailsFragment
    private fun openOutletDetails(outlet: Outlet) {
        val fragment = OutletDetailsFragment.newInstance(outlet)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // To navigate to AddOutletFragment
    private fun openAddOutlet() {
        val fragment = AddOutletFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        // Check if the navigation drawer is open
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Check if there are fragments in the back stack
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                // If neither the drawer is open nor fragments are in the back stack, call super
                super.onBackPressed()
            }
        }
    }


}

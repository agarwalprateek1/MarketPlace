package com.example.marketplace

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.marketplace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var i=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "MarketPlace"
        val popupMenu=PopupMenu(this,null)
        popupMenu.inflate(R.menu.bottom_menu)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragContainer)
        val navController=navHostFragment!!.findNavController()
        binding.bottomBar.setupWithNavController(popupMenu.menu,navController)
        binding.bottomBar.onItemSelected = {
            if(it==0)
                navController.navigate(R.id.homeFragment)
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            supportActionBar?.title = when (destination.id) {
                R.id.cartFragment -> "Your Cart"
                R.id.profileFragment -> "Profile"
                else -> "MarketPlace"
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(i==0){
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.contact_support -> {
                val i = Intent()
                i.action=Intent.ACTION_SENDTO
                i.data= Uri.parse("mailto:agarwalprateek520@gmail.com")
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
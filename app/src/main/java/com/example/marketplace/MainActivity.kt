package com.example.marketplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.marketplace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val popupMenu=PopupMenu(this,null)
        popupMenu.inflate(R.menu.bottom_menu)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragContainer)
        val navController=navHostFragment!!.findNavController()
        binding.bottomBar.setupWithNavController(popupMenu.menu,navController)
        navController.addOnDestinationChangedListener(object: NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                title= when(destination.id){
                    R.id.cartFragment -> "Your Cart"
                    R.id.profileFragment -> "Profile"
                    else -> "MarketPlace"
                }
            }
        })
    }
}
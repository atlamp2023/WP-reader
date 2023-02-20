package com.github.atlamp2023.wpreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.github.atlamp2023.wpreader.core.ShareViewModel
import com.github.atlamp2023.wpreader.core.util.Share
import com.github.atlamp2023.wpreader.core.util.State

const val TAG = "APP.EXCEPTION"

class MainActivity : AppCompatActivity(), Share {

    override val shareVM: ShareViewModel by lazy { ShareViewModel() }

    private val navController: NavController by lazy { getMainNavController() }

    init {
        shareVM.state.value = State.REMOTE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.show()
        setContentView(R.layout.activity_main)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean = (navController.navigateUp() || super.onSupportNavigateUp())

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    private fun getMainNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }
}
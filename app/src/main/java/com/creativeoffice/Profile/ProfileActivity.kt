package com.creativeoffice.Profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.BottomnavigationViewHelper
import kotlinx.android.synthetic.main.activity_home.*

class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


      //  setupNavigationView()


    }


    fun setupNavigationView() {

        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(this,bottomNavigationView) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

}

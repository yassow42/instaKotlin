package com.creativeoffice.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.BottomnavigationViewHelper
import com.creativeoffice.utils.HomePagerAdapter
import com.creativeoffice.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 0
    private val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        setupNavigationView()
        setupHomeViewPager()
        initImageLoader()

    }


    fun setupNavigationView() {

        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(
            this,
            bottomNavigationView
        ) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }


    private fun setupHomeViewPager() {

        var homePagerAdapter =
            HomePagerAdapter(supportFragmentManager) // fm bekledıgı ıcın supporFrag ıle kolayca halledılıyor.

        homePagerAdapter.addFragment(CameraFragment())
        homePagerAdapter.addFragment(HomeFragment()) // id = 1
        homePagerAdapter.addFragment(MessagesFragment())

        //activity_mainde bulunan view pagera olusturdugumuz adapterı atadık.
        homeViewPager.adapter = homePagerAdapter

        //view pagerı home ıle baslamasını sagladık
        homeViewPager.setCurrentItem(1)  // Bu pagerda id = 1 olanı ılk olarak aç

    }


    private fun initImageLoader() {

        var universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config)
    }


}

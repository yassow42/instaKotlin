package com.creativeoffice.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.creativeoffice.Login.LoginActivity
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.BottomnavigationViewHelper
import com.creativeoffice.utils.HomePagerAdapter
import com.creativeoffice.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener


    private val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        mAuth = FirebaseAuth.getInstance()

        setupAuthListener()
        setupHomeViewPager()
        initImageLoader()

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

    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    var intent = Intent(this@HomeActivity, LoginActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
                    )
                    startActivity(intent)
                    finish()
                } else {

                }


            }

        }
    }

    override fun onStart() {
        mAuth.addAuthStateListener(mAuthListener)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        homeViewPager.setCurrentItem(1)

    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

}

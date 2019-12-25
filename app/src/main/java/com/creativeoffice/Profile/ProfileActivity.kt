package com.creativeoffice.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.creativeoffice.Home.HomeActivity
import com.creativeoffice.Login.LoginActivity
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.BottomnavigationViewHelper
import com.creativeoffice.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.bottomNavigationView


class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"

    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mAuth= FirebaseAuth.getInstance()
        setupAuthListener()
        setupToolbar()
        setupNavigationView()
        setupProfilePhoto()



    }

    private fun setupProfilePhoto() {

        val imgURl= "i.imgyukle.com/2019/12/21/R6ekHS.jpg"
        val ilkKisim="https://"

UniversalImageLoader.setImage(imgURl,circleProfileImage,mProgressBarActivityProfile,ilkKisim)

    }


    override fun onBackPressed() {

        profileRoot.visibility = View.VISIBLE

        super.onBackPressed()
    }


    private fun setupToolbar() {

        imgProfileSettings.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ProfileSettingActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }

        tvProfilDuzenleButton.setOnClickListener {
            profileRoot.visibility = View.GONE

            val transaction = supportFragmentManager.beginTransaction()

            transaction.replace(R.id.profileContainer, ProfileEditFragment())
            transaction.addToBackStack("profil eklendi")
            transaction.commit()


        }

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


    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    var intent = Intent(this@ProfileActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
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

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

}

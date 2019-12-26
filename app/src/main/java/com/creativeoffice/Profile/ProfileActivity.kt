package com.creativeoffice.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.creativeoffice.Home.HomeActivity
import com.creativeoffice.Login.LoginActivity
import com.creativeoffice.Models.Users
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.BottomnavigationViewHelper
import com.creativeoffice.utils.EventbusDataEvents
import com.creativeoffice.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.bottomNavigationView
import org.greenrobot.eventbus.EventBus


class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"

    lateinit var mAuth: FirebaseAuth
    lateinit var mUser: FirebaseUser
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mRef = FirebaseDatabase.getInstance().reference


        setupToolbar()
        setupNavigationView()

        kullaniciBilgileriniGetir()


    }

    private fun kullaniciBilgileriniGetir() {

        mRef.child("users").child(mUser.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.getValue() != null) {
                    var okunanKullaniciBilgileri = p0.getValue(Users::class.java)

                    EventBus.getDefault().postSticky(EventbusDataEvents.KullaniciBilgileriniGonder(okunanKullaniciBilgileri))

                    tvPostSayisi.text = okunanKullaniciBilgileri!!.user_detail!!.post.toString()
                    tvProfilAdiToolbar.text = okunanKullaniciBilgileri.user_name.toString()
                    tvFollowerSayisi.text = okunanKullaniciBilgileri.user_detail!!.follower.toString()
                    tvFollowingSayisi.text = okunanKullaniciBilgileri.user_detail!!.following.toString()
                    tvProfilGercekAdi.text = okunanKullaniciBilgileri.adi_soyadi.toString()

                    //Profil fotosunu byle cagırıyoruz.
                    val imgURl: String = okunanKullaniciBilgileri.user_detail!!.profile_picture!!

                    UniversalImageLoader.setImage(
                        imgURl,
                        circleProfileImage, mProgressBarActivityProfile
                    )




                    if (okunanKullaniciBilgileri.user_detail!!.biography!!.isNotEmpty()) {

                        tvBiyografi.visibility = View.VISIBLE
                        tvBiyografi.text = okunanKullaniciBilgileri.user_detail!!.biography

                    }

                    if (okunanKullaniciBilgileri.user_detail!!.web_site!!.isNotEmpty()) {

                        tvWebSitesi.visibility = View.VISIBLE
                        tvWebSitesi.text = okunanKullaniciBilgileri.user_detail!!.web_site

                    }
                }


            }


        })

    }

    private fun setupProfilePhoto() {

        val imgURl = "https://i.imgyukle.com/2019/12/21/R6ekHS.jpg"


        UniversalImageLoader.setImage(imgURl, circleProfileImage, mProgressBarActivityProfile)

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
            profileContainer.visibility = View.VISIBLE
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
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    var intent = Intent(
                        this@ProfileActivity,
                        LoginActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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

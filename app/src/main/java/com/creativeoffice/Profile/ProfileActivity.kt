package com.creativeoffice.Profile

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativeoffice.Login.LoginActivity
import com.creativeoffice.Models.Posts
import com.creativeoffice.Models.UserPost
import com.creativeoffice.Models.Users
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.bottomNavigationView
import org.greenrobot.eventbus.EventBus


class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"
    lateinit var tumGonderiler: ArrayList<UserPost>

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

        tumGonderiler = ArrayList<UserPost>()
        setupToolbar()

        kullaniciBilgileriniGetir()
        kullaniciPostlariniGetir(mUser.uid)


        //ilk secilen her zaman imgGrid oldugundan rengi ilk basta mavı yaptık
        imgGrid.setColorFilter(ContextCompat.getColor(this,R.color.mavi),PorterDuff.Mode.SRC_IN )
        imgGrid.setOnClickListener {

            imgGrid.setColorFilter(ContextCompat.getColor(this,R.color.mavi),PorterDuff.Mode.SRC_IN )
            imgList.setColorFilter(ContextCompat.getColor(this,R.color.siyah),PorterDuff.Mode.SRC_IN )
            setupRecyclerView(1)
        }
        imgList.setOnClickListener {
            imgList.setColorFilter(ContextCompat.getColor(this,R.color.mavi),PorterDuff.Mode.SRC_IN )
            imgGrid.setColorFilter(ContextCompat.getColor(this,R.color.siyah),PorterDuff.Mode.SRC_IN)
            setupRecyclerView(2)
        }

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

    override fun onBackPressed() {

        profileRoot.visibility = View.VISIBLE
        bottomNaviContainer.visibility = View.VISIBLE


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
            bottomNaviContainer.visibility= View.GONE
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


    private fun kullaniciPostlariniGetir(kullaniciID: String) {

        mRef.child("users").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var userID = kullaniciID
                var kullaniciAdi = p0.getValue(Users::class.java)!!.user_name
                var kullaniciFotoUrl = p0.getValue(Users::class.java)!!.user_detail!!.profile_picture

                mRef.child("post").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0.hasChildren()) {


                            for (ds in p0.children) {
                                var eklenecekUserPost = UserPost() //boş contructer olusturdugumuz ıcın bu hata vermiyor.
                                eklenecekUserPost.userID = userID
                                eklenecekUserPost.userName = kullaniciAdi
                                eklenecekUserPost.userPhotoUrl = kullaniciFotoUrl
                                eklenecekUserPost.postID = ds.getValue(Posts::class.java)!!.post_id
                                eklenecekUserPost.postUrl = ds.getValue(Posts::class.java)!!.file_url
                                eklenecekUserPost.postAciklama = ds.getValue(Posts::class.java)!!.aciklama
                                eklenecekUserPost.postYuklenmeTarihi = ds.getValue(Posts::class.java)!!.yuklenme_tarihi

                                tumGonderiler.add(eklenecekUserPost)


                            }
                        }
                        setupRecyclerView(1)
                    }

                })

            }

        })


    }

    private fun setupRecyclerView(layoutCesidi: Int) {

        if (layoutCesidi == 1) {

            var kullaniciPostListe = profileRecyclerView

            kullaniciPostListe.adapter = ProfilePostGridAdapter(this,tumGonderiler)
            kullaniciPostListe.layoutManager = GridLayoutManager(this,3)


        }else if (layoutCesidi ==2 ){

            var kullaniciPostListe = profileRecyclerView

            kullaniciPostListe.adapter = ProfilePostListRecyclerAdapter(this,tumGonderiler)

            kullaniciPostListe.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        }


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

    override fun onResume() {
        setupNavigationView()
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

}

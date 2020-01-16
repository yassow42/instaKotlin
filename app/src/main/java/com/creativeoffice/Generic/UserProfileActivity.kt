package com.creativeoffice.Generic


import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativeoffice.Models.Posts
import com.creativeoffice.Models.UserPost
import com.creativeoffice.Models.Users
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_search.bottomNavigationView
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class UserProfileActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4

    lateinit var gelenID: String
    lateinit var tumGonderiler: ArrayList<UserPost>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setupNavigationView()

        var hazirID = "eEoeHechmoYHH3nurJIQcjB7T213"

        tumGonderiler = ArrayList()
        gelenID = intent.getStringExtra("arananKullaniciID")
        kullaniciBilgileriniGetir(gelenID)
        kullaniciPostlariniGetir(gelenID)
        setupToolbar()
        //ilk secilen her zaman imgGrid oldugundan rengi ilk basta mavı yaptık
        imgGrid.setColorFilter(ContextCompat.getColor(this, R.color.mavi), PorterDuff.Mode.SRC_IN)
        imgGrid.setOnClickListener {

            imgGrid.setColorFilter(ContextCompat.getColor(this, R.color.mavi), PorterDuff.Mode.SRC_IN)
            imgList.setColorFilter(ContextCompat.getColor(this, R.color.siyah), PorterDuff.Mode.SRC_IN)
            setupRecyclerView(1)
        }
        imgList.setOnClickListener {
            imgList.setColorFilter(ContextCompat.getColor(this, R.color.mavi), PorterDuff.Mode.SRC_IN)
            imgGrid.setColorFilter(ContextCompat.getColor(this, R.color.siyah), PorterDuff.Mode.SRC_IN)
            setupRecyclerView(2)
        }



    }

    private fun setupToolbar() {
        tvTakipButton.setOnClickListener {


        }

        imgBack.setOnClickListener {

            onBackPressed()
        }
    }

    private fun kullaniciBilgileriniGetir(gelenID: String) {

        FirebaseDatabase.getInstance().reference.child("users").child(gelenID).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

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


    private fun kullaniciPostlariniGetir(kullaniciID: String) {

        FirebaseDatabase.getInstance().reference.child("users").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var userID = kullaniciID
                var kullaniciAdi = p0.getValue(Users::class.java)!!.user_name
                var kullaniciFotoUrl = p0.getValue(Users::class.java)!!.user_detail!!.profile_picture

                FirebaseDatabase.getInstance().reference.child("post").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener {
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

            kullaniciPostListe.adapter = ProfilePostGridAdapter(this, tumGonderiler)
            kullaniciPostListe.layoutManager = GridLayoutManager(this, 3)


        } else if (layoutCesidi == 2) {

            var kullaniciPostListe = profileRecyclerView

            kullaniciPostListe.adapter = ProfilePostListRecyclerAdapter(this, tumGonderiler)

            kullaniciPostListe.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun setupNavigationView() {

        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(this, bottomNavigationView) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }


    /*
    //////////////////////eventbuss//////////////////////////
    @Subscribe(sticky = true)

    internal fun onDosyaEvent(eventGelenID: EventbusDataEvents.kullaniciIDgonder) {

        gelenID = eventGelenID.kullaniciID!!
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().unregister(this)

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().register(this)

    }
*/

}

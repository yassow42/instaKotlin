package com.creativeoffice.Search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.creativeoffice.Generic.UserProfileActivity

import com.creativeoffice.Models.Posts
import com.creativeoffice.Models.Users
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.BottomnavigationViewHelper
import com.creativeoffice.utils.EventbusDataEvents

import com.creativeoffice.utils.SearchActivityRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_search.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class SearchActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 1
    private val TAG = "SearchActivity"

    var tumGonderilerr = ArrayList<Posts>()
    lateinit var tumKullanicilar: Users


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        tumGonderilerr = ArrayList()

        setupNavigationView()
        setupRecyclerView()




        btnAra.setOnClickListener {
            arananKullanici()
            val intent = Intent(this,UserProfileActivity::class.java)

           startActivity(intent)

        }

    }


    private fun arananKullanici() {
        FirebaseDatabase.getInstance().reference.child("users").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (users in p0.children) {

                    tumKullanicilar = users.getValue(Users::class.java)!!

                    if (tumKullanicilar.user_name!!.equals(searchView.text.toString())) {

                        Log.e("userIDD", tumKullanicilar.user_id!!)

                        EventBus.getDefault().postSticky(EventbusDataEvents.kullaniciIDgonder(tumKullanicilar.user_id))

                        userProfileGit()

                    }else{
                        Log.e("user","boyle bırı yok")
                    }

                }

            }

        })
    }



    private fun setupRecyclerView() {

        FirebaseDatabase.getInstance().reference.child("all_post").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.hasChildren()) {
                    for (posts in p0.children) {
                        var alinanPost = posts.getValue(Posts::class.java)
                        tumGonderilerr.add(alinanPost!!)

                    }
                }
                var recyclerViewAdapter = SearchActivityRecyclerAdapter(this@SearchActivity, tumGonderilerr)
                recyclerSonGonderiler.adapter = recyclerViewAdapter
                //grid view seklinde gösterim yaptık.
                var layoutManagerr = GridLayoutManager(this@SearchActivity, 3)
                recyclerSonGonderiler.layoutManager = layoutManagerr
                recyclerSonGonderiler.setHasFixedSize(true)

                // LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            }
        })
    }


    fun setupNavigationView() {

        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(this, bottomNavigationView) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    override fun onResume() {
       // arananKullanici()
        super.onResume()
    }
}

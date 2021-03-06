package com.creativeoffice.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.creativeoffice.Generic.CommentFragment
import com.creativeoffice.Models.UserPost
import com.creativeoffice.Profile.ProfileActivity
import com.creativeoffice.instakotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.tek_post_recycler_item.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class ProfilePostListRecyclerAdapter(var myContext: Context, var tumGonderiler: ArrayList<UserPost>) : RecyclerView.Adapter<ProfilePostListRecyclerAdapter.MyViewHolder>() {

    init {//ilk calisan

        Collections.sort(tumGonderiler, object : Comparator<UserPost> {
            override fun compare(o1: UserPost?, o2: UserPost?): Int {
                if (o1!!.postYuklenmeTarihi!! > o2!!.postYuklenmeTarihi!!) {
                    return -1
                } else {
                    return 1
                }
            }
        })

    }

    override fun getItemCount(): Int {
        return tumGonderiler.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var viewHolder = LayoutInflater.from(myContext).inflate(R.layout.tek_post_recycler_item, parent, false)
        return MyViewHolder(viewHolder, myContext)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(position, tumGonderiler.get(position), myContext)



    }

    class MyViewHolder(itemView: View?, myContext: Context) : RecyclerView.ViewHolder(itemView!!) {


        var tumLayout = itemView as ConstraintLayout

        var profileImage = tumLayout.imgUserProfile
        var userNameTitle = tumLayout.tvKullaniciAdiBaslik
        var postImage = tumLayout.imgPostResim
        var userNameVeAciklama = tumLayout.tvKullaniciAdi

        var postKacZaman = tumLayout.tvKacZamanOnce
        var btnBegen = tumLayout.imgBegen
        var progresProfilFoto = tumLayout.pbUserProfile
        var progresPostFoto = tumLayout.pbPostFoto
        var yorumYap = tumLayout.imgYorum
        var begeniSayisi = tumLayout.tvBegeniSayisi
        var instaLike = tumLayout.insta_like_view

        fun setData(position: Int, oankiGonderi: UserPost, myProfileActivity: Context) {

            userNameTitle.text = oankiGonderi.userName
            var userName = "<font color =#000>" + " "  + oankiGonderi.userName +"</font>"+ " " + oankiGonderi.postAciklama
            var sonuc: Spanned? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                sonuc = Html.fromHtml(userName, Html.FROM_HTML_MODE_LEGACY)
            }

            userNameVeAciklama.text = sonuc


            var saat = TimeAgo.getTimeAgo(oankiGonderi.postYuklenmeTarihi!!)
            postKacZaman.text = saat

            UniversalImageLoader.setImage(oankiGonderi.userPhotoUrl!!, profileImage, progresProfilFoto)

            UniversalImageLoader.setImage(
                oankiGonderi.postUrl!!, postImage, progresPostFoto
            )



            yorumYap.setOnClickListener {

                EventBus.getDefault().postSticky(EventbusDataEvents.YorumYapilacakGonderiIDYolla(oankiGonderi.postID))

                myProfileActivity as ProfileActivity
                myProfileActivity.bottomNaviContainer.visibility = View.GONE
                myProfileActivity.profileRoot.visibility = View.GONE
                myProfileActivity.profileContainer.visibility = View.VISIBLE
                //geriye gelince kapal? kal?yordu homeactivity de tekrar actik.

                var transaction = myProfileActivity.supportFragmentManager.beginTransaction()
                transaction.addToBackStack("comment eklendi")
                transaction.replace(R.id.profileContainer, CommentFragment())
                transaction.commit()

            }
            btnBegen.setOnClickListener {

                var mUserID = FirebaseAuth.getInstance().currentUser!!.uid
                var mRef = FirebaseDatabase.getInstance().reference.child("likes").child(oankiGonderi.postID!!)
                mRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0.hasChild(mUserID)) {
                            btnBegen.setImageResource(R.drawable.ic_begen_red)
                            mRef.child(mUserID).removeValue()

                        } else {
                            mRef.child(mUserID).setValue(mUserID)
                            btnBegen.setImageResource(R.drawable.ic_begen)
                        }
                    }
                })
            }

            //fotograf begeni durumu.
            FirebaseDatabase.getInstance().reference.child("likes").child(oankiGonderi.postID!!).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var mUserID = FirebaseAuth.getInstance().currentUser!!.uid
                    if (p0.hasChild(mUserID)) {
                        btnBegen.setImageResource(R.drawable.ic_begen_red)
                    } else {
                        btnBegen.setImageResource(R.drawable.ic_begen)
                    }

                    if (p0.exists()) {
                        begeniSayisi.visibility = View.VISIBLE
                        begeniSayisi.text = p0!!.childrenCount.toString() + "  begenen"
                    } else {
                        begeniSayisi.visibility = View.GONE

                    }


                }
            })
            ////////////////
            ///?ift t?klama
            var ilkTiklama: Long = 0
            var sonTiklama: Long = 0
            postImage.setOnClickListener {
                ilkTiklama = sonTiklama
                sonTiklama = System.currentTimeMillis()

                if (sonTiklama - ilkTiklama < 300) {
                    instaLike.start()
                    sonTiklama = 0

                    var mUserID = FirebaseAuth.getInstance().currentUser!!.uid
                    var mRef = FirebaseDatabase.getInstance().reference.child("likes").child(oankiGonderi.postID!!)
                    mRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.hasChild(mUserID)) {
                                btnBegen.setImageResource(R.drawable.ic_begen_red)
                                mRef.child(mUserID).removeValue()
                            } else {
                                mRef.child(mUserID).setValue(mUserID)
                                btnBegen.setImageResource(R.drawable.ic_begen)
                            }
                        }
                    })
                }
            }
            /////////?ift t?klama ////////////////
        }
    }
}
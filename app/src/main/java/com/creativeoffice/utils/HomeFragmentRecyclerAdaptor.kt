package com.creativeoffice.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.creativeoffice.Generic.CommentFragment
import com.creativeoffice.Home.HomeActivity
import com.creativeoffice.Models.UserPost
import com.creativeoffice.instakotlin.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.tek_post_recycler_item.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class HomeFragmentRecyclerAdaptor(var myContext: Context, var tumGonderiler: ArrayList<UserPost>) : RecyclerView.Adapter<HomeFragmentRecyclerAdaptor.MyViewHolder>() {

    init {//ilk cal?san

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
        var userName = tumLayout.tvKullaniciAdi
        var postAciklama = tumLayout.tvPostAciklama
        var postKacZaman = tumLayout.tvKacZamanOnce

        var progresProfilFoto = tumLayout.pbUserProfile
        var progresPostFoto = tumLayout.pbPostFoto

        var yorumYap = tumLayout.imgYorum

        fun setData(position: Int, oankiGonderi: UserPost, myContext: Context) {

            userNameTitle.text = oankiGonderi.userName
            userName.text = oankiGonderi.userName
            postAciklama.text = oankiGonderi.postAciklama

            var saat = TimeAgo.getTimeAgo(oankiGonderi.postYuklenmeTarihi!!)
            postKacZaman.text = saat

            UniversalImageLoader.setImage(oankiGonderi.userPhotoUrl!!, profileImage, progresProfilFoto)

            UniversalImageLoader.setImage(
                oankiGonderi.postUrl!!, postImage, progresPostFoto
            )



            yorumYap.setOnClickListener {

                EventBus.getDefault().postSticky(EventbusDataEvents.YorumYapilacakGonderiIDYolla(oankiGonderi.postID))

                myContext as HomeActivity
                myContext.homeRoot.visibility = View.GONE
                myContext.homeContainer.visibility = View.VISIBLE
                //geriye gelince kapal? kal?yordu homeactivity de tekrar actik.

                var transaction = myContext.supportFragmentManager.beginTransaction()
                transaction.addToBackStack("comment eklendi")
                transaction.replace(R.id.homeContainer, CommentFragment())
                transaction.commit()

            }

        }

    }
}
package com.creativeoffice.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.creativeoffice.Models.UserPost
import com.creativeoffice.instakotlin.R
import kotlinx.android.synthetic.main.tek_post_recycler_item.view.*
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
            }})

    }

    override fun getItemCount(): Int {
        return tumGonderiler.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var viewHolder = LayoutInflater.from(myContext).inflate(R.layout.tek_post_recycler_item, parent, false)
        return MyViewHolder(viewHolder)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(position, tumGonderiler.get(position))


    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {


        var tumLayout = itemView as ConstraintLayout

        var profileImage = tumLayout.imgUserProfile
        var userNameTitle = tumLayout.tvKullaniciAdiBaslik
        var postImage = tumLayout.imgPostResim
        var userName = tumLayout.tvKullaniciAdi
        var postAciklama = tumLayout.tvPostAciklama
        var postKacZaman = tumLayout.tvKacZamanOnce

        var progresProfilFoto = tumLayout.pbUserProfile
        var progresPostFoto = tumLayout.pbPostFoto

        fun setData(position: Int, oankiGonderi: UserPost) {

            userNameTitle.text = oankiGonderi.userName
            userName.text = oankiGonderi.userName
            postAciklama.text = oankiGonderi.postAciklama

            var saat = TimeAgo.getTimeAgo(oankiGonderi.postYuklenmeTarihi!!)
            postKacZaman.text = saat

            UniversalImageLoader.setImage(oankiGonderi.userPhotoUrl!!, profileImage, progresProfilFoto)

            UniversalImageLoader.setImage(
                oankiGonderi.postUrl!!, postImage, progresPostFoto
            )


        }

    }
}
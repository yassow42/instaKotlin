package com.creativeoffice.Generic


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creativeoffice.Models.Comments
import com.creativeoffice.Models.Users

import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.EventbusDataEvents
import com.creativeoffice.utils.TimeAgo
import com.creativeoffice.utils.UniversalImageLoader
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.fragment_comment.view.*
import kotlinx.android.synthetic.main.tek_yorum.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class CommentFragment : Fragment() {


    var yorumYapilacakGonderiID: String? = null


    lateinit var mAuth: FirebaseAuth
    lateinit var mUser: FirebaseUser
    lateinit var mRef: DatabaseReference
    lateinit var myAdapter: FirebaseRecyclerAdapter<Comments, CommentViewHolder>
    lateinit var fragmentView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_comment, container, false)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!


        setupCommentsRecyclerView()
        setupPaylasButton()
        setupProfilPicture()
        return fragmentView
    }


    private fun setupPaylasButton() {
        fragmentView.tvBtnPaylas.setOnClickListener {

            if (!etYorum.text.isNullOrEmpty()) {

                var yeniYorum = hashMapOf<String, Any>("user_id" to mUser.uid, "yorum" to etYorum.text.toString(), "yorum_begeni" to "0", "yorum_tarih" to ServerValue.TIMESTAMP)

                FirebaseDatabase.getInstance().reference.child("comments").child(yorumYapilacakGonderiID!!).push().setValue(yeniYorum)
            }

            etYorum.setText("")
        }
    }

    private fun setupProfilPicture() {

        FirebaseDatabase.getInstance().reference.child("users").child(mUser.uid).child("user_detail").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var kullaniciPfimg = p0!!.child("profile_picture").getValue().toString()
                UniversalImageLoader.setImage(kullaniciPfimg!!, yorumCircleimg, yorumCirclePB)
            }
        })
    }

    private fun setupCommentsRecyclerView() {
        mRef = FirebaseDatabase.getInstance().reference.child("comments").child(yorumYapilacakGonderiID!!)
        val options = FirebaseRecyclerOptions.Builder<Comments>()
            .setQuery(mRef, Comments::class.java)
            .build()


        myAdapter = object : FirebaseRecyclerAdapter<Comments, CommentViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {

                var CommentviewHolder = LayoutInflater.from(activity).inflate(R.layout.tek_yorum, parent, false)

                return CommentViewHolder(CommentviewHolder)

            }

            override fun onBindViewHolder(holder: CommentViewHolder, p1: Int, model: Comments) {


                holder.setData(model)

            }
        }

        fragmentView!!.yorumlarRecyclerView.adapter = myAdapter
        fragmentView!!.yorumlarRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tumCommentLayout = itemView as ConstraintLayout
        var profileImg = tumCommentLayout.yorumYapanProfilPhoto
        var tvUserveAciklama = tumCommentLayout.tvUserAciklama
        var tvYorumZamani = tumCommentLayout.tvYorumZaman
        var tvBegeniSayi = tumCommentLayout.tvBegeniSayi
        var yorumLike = tumCommentLayout.imgYorumLike

        fun setData(oAnOlusturulanYorum: Comments) {
            tvYorumZamani.text = TimeAgo.getTimeAgoComments(oAnOlusturulanYorum.yorum_tarih!!.toLong())

            tvBegeniSayi.text = "${oAnOlusturulanYorum.yorum_begeni} begeni"
            kullaniciVerileriniGetir(oAnOlusturulanYorum.user_id, oAnOlusturulanYorum.yorum)
        }

        private fun kullaniciVerileriniGetir(userID: String?, yorum: String?) {
            var mRef = FirebaseDatabase.getInstance().reference.child("users")
            mRef.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var userName = "<font color =#000000>" + " " + p0.getValue(Users::class.java)!!.user_name.toString() + "</font>" + " " + yorum
                    var sonuc: Spanned? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                        sonuc = Html.fromHtml(userName, Html.FROM_HTML_MODE_LEGACY)
                    }


                    tvUserveAciklama.text = sonuc

                    var imgUrl = p0!!.getValue(Users::class.java)!!.user_detail!!.profile_picture
                    UniversalImageLoader.setImage(imgUrl.toString(), profileImg, null)
                }
            })
        }
    }


    override fun onStart() {
        myAdapter.startListening()
        super.onStart()
    }


    override fun onStop() {

        myAdapter.stopListening()
        super.onStop()
    }


    //////////////////////eventbuss//////////////////////////
    @Subscribe(sticky = true)

    internal fun onDosyaEvent(gelenID: EventbusDataEvents.YorumYapilacakGonderiIDYolla) {

        yorumYapilacakGonderiID = gelenID.gonderiID
    }

    override fun onAttach(context: Context) {
        EventBus.getDefault().register(this)
        super.onAttach(context)
    }

    override fun onDetach() {
        EventBus.getDefault().unregister(this)
        super.onDetach()
    }


}
package com.creativeoffice.Share


import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.creativeoffice.Models.Posts
import com.creativeoffice.Profile.YukleniyorFragment

import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.DosyaIslemleri
import com.creativeoffice.utils.EventbusDataEvents
import com.creativeoffice.utils.UniversalImageLoader
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_share_next.*
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import kotlinx.android.synthetic.main.fragment_yukleniyor.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class ShareNextFragment : Fragment() {
    var secilenResimYolu: String? = null
    var dosyaTuruResimMi: Boolean? = null

    lateinit var photoURI: Uri

    lateinit var mAuth: FirebaseAuth
    lateinit var mUser: FirebaseUser
    lateinit var mRef: DatabaseReference
    lateinit var mStorageReference: StorageReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_share_next, container, false)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mRef = FirebaseDatabase.getInstance().reference
        mStorageReference = FirebaseStorage.getInstance().reference

        UniversalImageLoader.setImage("file://" + secilenResimYolu!!, view!!.imageView3, view.progressBar2)
        photoURI = Uri.parse("file://" + secilenResimYolu)




        view.tvPaylasButton.setOnClickListener {
            //resim dosyası sıkıştırma
            if (dosyaTuruResimMi == true) {

                DosyaIslemleri.compressResimDosya(this, secilenResimYolu!!)


            }//videoları sıkıstırır
            else if (dosyaTuruResimMi == false) {


            }


        }




        return view
    }

    private fun veritabaninaBilgileriYaz(yuklenenPhotoURl: String?) {
        var postID = mRef.child("post").child(mUser!!.uid).push().key
        var yuklenenPost = Posts(mUser!!.uid, postID, "", etPostAciklama.text.toString(), yuklenenPhotoURl)

        mRef.child("post").child(mUser!!.uid).child(postID!!).setValue(yuklenenPost)
        mRef.child("post").child(mUser!!.uid).child(postID!!).child("yuklenme_tarihi").setValue(ServerValue.TIMESTAMP)


    }


    //////////////////////eventbuss//////////////////////////
    @Subscribe(sticky = true)

    internal fun onResimEvent(secilenResim: EventbusDataEvents.PaylasilacakResmiGonder) {
        secilenResimYolu = secilenResim.resimYol
        dosyaTuruResimMi = secilenResim.dosyaTuruResimMi
    }

    override fun onAttach(context: Context) {
        EventBus.getDefault().register(this)
        super.onAttach(context)
    }

    override fun onDetach() {
        EventBus.getDefault().unregister(this)
        super.onDetach()
    }

    fun uploadStoage(filePath: String?) {

        var fileUrl = Uri.parse("file://" + filePath)

        var dialogYukleniyor = YukleniyorFragment()
        dialogYukleniyor.show(activity!!.supportFragmentManager, "Yuklenıyor")
        dialogYukleniyor.isCancelable = false

        mStorageReference.child("users").child(mUser!!.uid).child(fileUrl.lastPathSegment!!).putFile(fileUrl)
            .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {
                override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {

                    if (p0.isSuccessful) {
                        dialogYukleniyor.dismiss()

                        veritabaninaBilgileriYaz(p0.result!!.uploadSessionUri.toString()) //Yuklenen fotografın url sını aldık.
                    }

                }

            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    dialogYukleniyor.dismiss()
                    Toast.makeText(activity, "Hata Oluştu" + p0!!.message, Toast.LENGTH_SHORT).show()
                }
            })
            .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot> {
                override fun onProgress(p0: UploadTask.TaskSnapshot) {

                    var progress = 100 * p0.bytesTransferred / p0.totalByteCount
                    dialogYukleniyor.tvBilgi.text = "% " + progress.toInt().toString()+" yüklendi..."

                }

            })


    }

}

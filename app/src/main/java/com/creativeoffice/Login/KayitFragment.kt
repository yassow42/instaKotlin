package com.creativeoffice.Login


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.creativeoffice.Models.Users

import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.EventbusDataEvents
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_kayit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class KayitFragment : Fragment() {
    var telNo = ""
    var verificationID = ""
    var gelenKod = ""
    var gelenEmail = ""
    var emailIleKayitIslemi = true

    lateinit var mAuth: FirebaseAuth
    lateinit var mRef: DatabaseReference
    lateinit var progresBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_kayit, container, false)

        mAuth = FirebaseAuth.getInstance()
        progresBar = view.pbKullaniciKayit

        if (mAuth.currentUser != null) {
            mAuth.signOut()
        }


        mRef = FirebaseDatabase.getInstance().reference


        view.btnGiris.setOnClickListener {

            progresBar.visibility = View.VISIBLE
            //kullanıcı e mail ile kaydolma ıstyr
            if (emailIleKayitIslemi) {

                var sifre = view.etSifre.text.toString()
                var userName = view.etKullaniciAdi.text.toString()
                var adSoyad = view.etAdSoyad.text.toString()


                mAuth.createUserWithEmailAndPassword(gelenEmail, sifre)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                        override fun onComplete(p0: Task<AuthResult>) {

                            if (p0.isSuccessful) {

                                var userID =
                                    mAuth.currentUser!!.uid.toString() //burdan alacagız userID cunku kullanıcının bir oluşması lazım

                                Toast.makeText(
                                    activity!!,
                                    "oturum açıldı mail ile " + mAuth.currentUser!!.uid,
                                    Toast.LENGTH_LONG
                                ).show()

                                //oturum açan kullancının verilerini databaseye kaydetme

                                var kaydedilecekKullanici =
                                    Users(gelenEmail, "", sifre, userName, userID, adSoyad, "")

                                mRef.child("users").child(userID).setValue(kaydedilecekKullanici)
                                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                                        override fun onComplete(p0: Task<Void>) {
                                            if (p0.isSuccessful) {

                                                progresBar.visibility = View.INVISIBLE


                                                Toast.makeText(
                                                    activity!!,
                                                    "kullanici kaydedildi.",
                                                    Toast.LENGTH_LONG
                                                ).show()



                                            } else {
                                                //eger kullanici oturum actı ama databaseye kaydedilmediyse kullanıcıyı sılıyoruz. Register Activitye tekrar göndeririz.
                                                mAuth.currentUser!!.delete().addOnCompleteListener {
                                                    if (p0.isSuccessful) {
                                                        progresBar.visibility = View.INVISIBLE
                                                        Toast.makeText(
                                                            activity!!,
                                                            "kullanici kaydedilemedi, Tekrar deneyin...",
                                                            Toast.LENGTH_LONG
                                                        ).show()


                                                    }
                                                }

                                            }
                                        }
                                    })


                            } else {
                                progresBar.visibility = View.INVISIBLE
                                Toast.makeText(
                                    activity!!,
                                    "oturum açılamadı: " + p0.exception,
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }

                        }
                    })

                //kullanıcı tel ıle kaydolmak ıstyr
            } else {


                var sifre = view.etSifre.text.toString()
                var sahteEmail = telNo + "@gmail.com"
                var userName = view.etKullaniciAdi.text.toString()
                var adSoyad = view.etAdSoyad.text.toString()

                mAuth.createUserWithEmailAndPassword(sahteEmail, sifre)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                        override fun onComplete(p0: Task<AuthResult>) {

                            if (p0.isSuccessful) {
                                var userID = mAuth.currentUser!!.uid.toString()
                                Toast.makeText(
                                    activity!!,
                                    "oturum tel no ile açıldı " + mAuth.currentUser!!.uid,
                                    Toast.LENGTH_LONG
                                ).show()
                                progresBar.visibility = View.INVISIBLE

                                var kaydedilecekKullanici =
                                    Users("", sahteEmail, sifre, userName, userID, adSoyad, telNo)
                                mRef.child("users").child(userID).setValue(kaydedilecekKullanici)
                                    .addOnCompleteListener {

                                        if (p0.isSuccessful) {

                                            progresBar.visibility = View.INVISIBLE
                                            Toast.makeText(
                                                activity!!,
                                                "oturum tel ile acıldı.",
                                                Toast.LENGTH_LONG
                                            ).show()

                                        } else {
                                            //eger kullanici oturum actı ama databaseye kaydedilmediyse kullanıcıyı sılıyoruz. Register Activitye tekrar göndeririz.
                                            mAuth.currentUser!!.delete().addOnCompleteListener {
                                                if (p0.isSuccessful) {
                                                    Toast.makeText(
                                                        activity!!,
                                                        "kullanici kaydedilemedi, Tekrar deneyin...",
                                                        Toast.LENGTH_LONG
                                                    ).show()

                                                }
                                            }
                                        }
                                    }


                            } else {
                                Toast.makeText(
                                    activity!!,
                                    "oturum açılamadı: " + p0.exception,
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }

                        }
                    })

            }

        }

        return view
    }


    //////////////////////eventbuss//////////////////////////
    @Subscribe(sticky = true)

    internal fun onKayitEvent(kayitBilgileri: EventbusDataEvents.KayitBilgileriniGonder) {

        if (kayitBilgileri.emailKayit == true) {
            emailIleKayitIslemi = true
            gelenEmail = kayitBilgileri.email!!
        } else {
            emailIleKayitIslemi = false
            telNo = kayitBilgileri.telNo!!
            verificationID = kayitBilgileri.verificationID!!
            gelenKod = kayitBilgileri.code!!
        }

    }


    override fun onAttach(context: Context) {

        EventBus.getDefault().register(this)
        super.onAttach(context)
    }

    override fun onDetach() {

        EventBus.getDefault().unregister(this)
        super.onDetach()
    }

    ///////////////////////eventbuss////////////////////////

}

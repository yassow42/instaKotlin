package com.creativeoffice.Login


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.EventbusDataEvents
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.*
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 */
class TelefonKoduGirFragment : Fragment() {

    var gelenTelNo = ""

    lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificationID = ""
    var gelenKod = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_telefon_kodu_gir, container, false)

        view.tvKullaniciTelNo.text = gelenTelNo

        setupCallBack()

        view.btnTelKodİleri.setOnClickListener {


            if (gelenKod.equals(view.etOnayKodu.text.toString())) {

                val transaction = activity!!.supportFragmentManager.beginTransaction()

                transaction.replace(R.id.loginContainer, KayitFragment())
                transaction.addToBackStack("TelefonKodu")
                transaction.commit()


            } else {


                val transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, KayitFragment())
                transaction.addToBackStack("Kayıt fragment")
                transaction.commit()


                Toast.makeText(activity, "olmadı mk", Toast.LENGTH_LONG).show()

            }

        }



        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(gelenTelNo, 60, TimeUnit.SECONDS, activity!!, mCallBack)




        return view
    }

    private fun setupCallBack() {

        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                gelenKod = credential.smsCode!!

            }

            override fun onVerificationFailed(e: FirebaseException) {

                Log.e("hatalar", e.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                verificationID = verificationId!!
            }
        }

    }


    //Buradan da mesajı alıyorr.
    @Subscribe(sticky = true)
    internal fun onTelefonNoEvent(telefonNumarasi: EventbusDataEvents.TelefonNoGonder) {

        gelenTelNo = telefonNumarasi.telNo

        Log.e("yasin", "gelen no :  $gelenTelNo")
    }


    //bu fragment bı yerlerden mesaj alıcak. Eventbus yayınından yanii
    override fun onAttach(context: Context) {

        EventBus.getDefault().register(this)
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()

        EventBus.getDefault().unregister(this)
    }
}

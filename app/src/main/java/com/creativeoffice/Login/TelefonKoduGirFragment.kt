package com.creativeoffice.Login


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.EventbusDataEvents
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 */
class TelefonKoduGirFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view = inflater.inflate(R.layout.fragment_telefon_kodu_gir, container, false)


        return view
    }


    //Buradan da mesajı alıyorr.
    @Subscribe (sticky = true)
    internal fun onTelefonNoEvent(telefonNumarasi: EventbusDataEvents.TelefonNoGonder){

        var gelenTelNo = telefonNumarasi.telNo

       Log.e("yasin","gelen no :  $gelenTelNo")
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

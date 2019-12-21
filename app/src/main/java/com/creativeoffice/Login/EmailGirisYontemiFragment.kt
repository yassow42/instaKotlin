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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class EmailGirisYontemiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_email_giris_yontemi, container, false)

        return view
    }

@Subscribe (sticky = true)

internal fun onEmailEvent(emailGelen:EventbusDataEvents.EmailGonder){

    var gelenEmail = emailGelen.email

    Log.e("yasin",gelenEmail)
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

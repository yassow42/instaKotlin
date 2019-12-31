package com.creativeoffice.Share


import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.EventbusDataEvents
import com.creativeoffice.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.fragment_share_next.*
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class ShareNextFragment : Fragment() {
    var secilenResimYolu: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_share_next, container, false)
      //  view.imageView3.setImageURI(Uri.parse(secilenResimYolu))

        UniversalImageLoader.setImage("file://"+secilenResimYolu!!,view!!.imageView3,view.progressBar2)


        view.tvPaylasButton.setOnClickListener {


        }

        return view
    }














    //////////////////////eventbuss//////////////////////////
    @Subscribe(sticky = true)

    internal fun onResimEvent(secilenResim: EventbusDataEvents.PaylasilacakResmiGonder) {
        secilenResimYolu = secilenResim.resimYol
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

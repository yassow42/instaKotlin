package com.creativeoffice.Share


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.DosyaIslemleri
import com.creativeoffice.utils.EventbusDataEvents
import com.creativeoffice.utils.ShareActivityGridViewAdapter
import com.creativeoffice.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ShareGalleryFragment : Fragment() {

    var secilenResimYolu: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_share_gallery, container, false)


        var klasorPaths = ArrayList<String>()
        var klasorAdlari = ArrayList<String>()

        var root = Environment.getExternalStorageDirectory().path


        var kameraResimleri = root + "/DCIM/Camera"
        var indirilenResimler = root + "/Download"
        var whatsappResimleri = root + "/WhatsApp/Media/WhatsApp Images"
        var videolar = root + "/Video"

        Log.e("Hata", kameraResimleri)
        Log.e("Hata", whatsappResimleri)
        Log.e("Hata", indirilenResimler)

        klasorPaths.add(kameraResimleri)
        klasorPaths.add(indirilenResimler)
        klasorPaths.add(whatsappResimleri)
        klasorPaths.add(videolar)


        klasorAdlari.add("Kamera")
        klasorAdlari.add("İndirilenler")
        klasorAdlari.add("WhatsApp")
        klasorAdlari.add("Videolar")


        var spinnerArrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, klasorAdlari)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        view.spnKlasorAdlari.adapter = spinnerArrayAdapter
        view.spnKlasorAdlari.setSelection(0)

        //ilk acıldıgında en son dosya gosterilir.

        view.spnKlasorAdlari.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setupGridView(DosyaIslemleri.klasordekiDosyalariGetir(klasorPaths.get(position)))
            }

        }

        view.tvİleriButton.setOnClickListener {
            activity!!.anaLayout.visibility = View.GONE
            activity!!.fragmentContainerLayout.visibility = View.VISIBLE
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            ////////////////////// EventBus yayın yaptık.
            EventBus.getDefault().postSticky(EventbusDataEvents.PaylasilacakResmiGonder(secilenResimYolu))
            //////////////////////
            transaction.addToBackStack("ShareNexteklendi")
            transaction.replace(R.id.fragmentContainerLayout, ShareNextFragment())
            transaction.commit()


        }




        return view
    }


    fun setupGridView(secilenKlasordekiDosyalar: ArrayList<String>) {

        var gridAdapter = ShareActivityGridViewAdapter(activity!!, R.layout.tek_satir_grid_resim, secilenKlasordekiDosyalar)

        gridResimler.adapter = gridAdapter
        if (secilenKlasordekiDosyalar.size > 0) {
            secilenResimYolu = secilenKlasordekiDosyalar.get(0)
            resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(0))

        } else {
            videoView.visibility = View.INVISIBLE
            imgCropView.visibility = View.INVISIBLE

        }

        gridResimler.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                secilenResimYolu = secilenKlasordekiDosyalar.get(position)
                resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(position))


            }

        })


    }

    fun resimVeyaVideoGoster(dosyaYolu: String) {

        var dosyaTuru = dosyaYolu.substring(dosyaYolu.lastIndexOf("."))

        if (dosyaTuru != null) {

            if (dosyaTuru.equals(".mp4")) {
                videoView.visibility = View.VISIBLE
                imgCropView.visibility = View.INVISIBLE
                videoView.setVideoURI(Uri.parse("file://" + dosyaYolu))
                videoView.start()
                pbImgBuyukResim.visibility = View.GONE

            } else {
                videoView.visibility = View.INVISIBLE
                imgCropView.visibility = View.VISIBLE

                UniversalImageLoader.setImage("file:/" + dosyaYolu, imgCropView, pbImgBuyukResim)
            }

        }


    }




}

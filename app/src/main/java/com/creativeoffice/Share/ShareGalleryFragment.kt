package com.creativeoffice.Share


import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.DosyaIslemleri
import com.creativeoffice.utils.ShareActivityGridViewAdapter
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*

class ShareGalleryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_share_gallery, container, false)

        Toast.makeText(activity,"galeri",Toast.LENGTH_LONG).show()

        var klasorPaths = ArrayList<String>()
        var klasorAdlari = ArrayList<String>()

        var root =  Environment.getExternalStorageDirectory().path


        var kameraResimleri= root+"/DCIM/Camera"
        var indirilenResimler=root+"/Download"
        var whatsappResimleri=root+"/WhatsApp/Media/WhatsApp Images"

        Log.e("Hata", kameraResimleri)
        Log.e("Hata", whatsappResimleri)
        Log.e("Hata", indirilenResimler)

        klasorPaths.add(kameraResimleri)
        klasorPaths.add(indirilenResimler)
        klasorPaths.add(whatsappResimleri)


        klasorAdlari.add("Kamera")
        klasorAdlari.add("İndirilenler")
        klasorAdlari.add("WhatsApp")


        var spinnerArrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, klasorAdlari)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        view.spnKlasorAdlari.adapter = spinnerArrayAdapter
        view.spnKlasorAdlari.setSelection(0)

        view.spnKlasorAdlari.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var klasordekiDosyalar = DosyaIslemleri.klasordekiDosyalariGetir(kameraResimleri)

                var gridAdapter = ShareActivityGridViewAdapter(activity!!,R.layout.tek_satir_grid_resim,klasordekiDosyalar)

                gridResimler.adapter=gridAdapter

               /* for (str in klasordekiDosyalar) {

                    Log.e("Hata", str)
                }
                */
            }

        }




        return view
    }


}

package com.creativeoffice.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.creativeoffice.instakotlin.R
import kotlinx.android.synthetic.main.tek_satir_grid_resim.view.*

class ShareActivityGridViewAdapter(context: Context, resource: Int, var klasordekiDosyalar: ArrayList<out String>) : ArrayAdapter<String>(context, resource, klasordekiDosyalar) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tek_sutun_resim = convertView // listViewda  "3" adlı inflater resim sayısı kadar cevrılıyor eger 500 resım varsa 500ude cevrıldıgı ıcın sıstemı zorlar. Bizde convertView kullanarak her
        // kaydırısta 6 veya 10 resim glemesini saglıyoruz.
        var inflater = LayoutInflater.from(context) //"3"

        if (tek_sutun_resim==null){
            tek_sutun_resim = inflater.inflate(R.layout.tek_satir_grid_resim, parent, false)
        }


        var imgView = tek_sutun_resim!!.imgTekSutunImage
        var progressBar = tek_sutun_resim.pbTekSutunImage
        var imgURL = klasordekiDosyalar.get(position)


        UniversalImageLoader.setImage("file:/" + imgURL, imgView, progressBar)

        return tek_sutun_resim
    }
}
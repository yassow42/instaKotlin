package com.creativeoffice.utils

import android.util.Log
import java.io.File

class DosyaIslemleri {

    companion object {

        fun klasordekiDosyalariGetir(klasorAdi: String): ArrayList<String> {//demek ki arraylist olarak birşey donmeli

            var tumDosyalar = ArrayList<String>()



            var file = File(klasorAdi) //Bu File() ile okuyoruz. altta da tum dosyaları liste olarak atıyor..

            var klasordekiTumDosyalar = file.listFiles() // liste olarak atıyor.



            //gonderdigimiz klasor yolunda eleman var mı yok mu
            if (klasordekiTumDosyalar != null) {

                for (i in 0 .. klasordekiTumDosyalar.size-1) {
                    if (klasordekiTumDosyalar[i].isFile) {

                        Log.e("Hata","okunan veri bir dosya")

                        //files://root/logo.png absolutepath alıyor
                        var okunanDosyaYolu = klasordekiTumDosyalar[i].absolutePath

                        Log.e("Hata","okunan dosya yolu " + okunanDosyaYolu)

                        var dosyaTuru = okunanDosyaYolu.substring(okunanDosyaYolu.lastIndexOf(".")) //nokta dahil olarak okur.

                       Log.e("Hata","okunan dosya turu " + dosyaTuru)

                        if (dosyaTuru.equals(".jpg") || dosyaTuru.equals(".jpeg") || dosyaTuru.equals(".mp4")) {

                            tumDosyalar.add(okunanDosyaYolu)
                        }
                    }
                }
            }else{
                Log.e("Hata", "Klasorler Bos")
            }


            return tumDosyalar
        }
    }
}
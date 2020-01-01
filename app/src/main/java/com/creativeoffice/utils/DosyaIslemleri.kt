package com.creativeoffice.utils

import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import com.creativeoffice.Profile.YukleniyorFragment
import com.creativeoffice.Share.ShareNextFragment
import com.iceteck.silicompressorr.SiliCompressor
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class DosyaIslemleri {

    companion object {

        fun klasordekiDosyalariGetir(klasorAdi: String): ArrayList<String> {//demek ki arraylist olarak birşey donmeli

            var tumDosyalar = ArrayList<String>()


            var file = File(klasorAdi) //Bu File() ile okuyoruz. altta da tum dosyaları liste olarak atıyor..

            var klasordekiTumDosyalar = file.listFiles() // liste olarak atıyor.


            //gonderdigimiz klasor yolunda eleman var mı yok mu
            if (klasordekiTumDosyalar != null) {

                if (klasordekiTumDosyalar.size > 1) { // burada dosyaları tarihe gore sıraladık ozel bır metod burası

                    Arrays.sort(klasordekiTumDosyalar, object : Comparator<File> {
                        override fun compare(o1: File?, o2: File?): Int {

                            if (o1!!.lastModified() > o2!!.lastModified()) {
                                return -1
                            } else {
                                return 1
                            }
                        }

                    })
                }

                for (i in 0..klasordekiTumDosyalar.size - 1) {
                    if (klasordekiTumDosyalar[i].isFile) {

                      //  Log.e("Hata", "okunan veri bir dosya")

                        //files://root/logo.png absolutepath alıyor
                        var okunanDosyaYolu = klasordekiTumDosyalar[i].absolutePath

                      //  Log.e("Hata", "okunan dosya yolu " + okunanDosyaYolu)

                        var dosyaTuru = okunanDosyaYolu.substring(okunanDosyaYolu.lastIndexOf(".")) //nokta dahil olarak okur.

                      //  Log.e("Hata", "okunan dosya turu " + dosyaTuru)

                        if (dosyaTuru.equals(".jpg") || dosyaTuru.equals(".jpeg") || dosyaTuru.equals(".mp4")) {

                            tumDosyalar.add(okunanDosyaYolu)
                        }
                    }
                }
            } else {


                Log.e("Hata", "Klasorler Bos")
            }


            return tumDosyalar
        }

        fun compressResimDosya(fragment: Fragment, secilenResimYolu: String) {
            ResimCompressAsyncTask(fragment).execute(secilenResimYolu)

        }
    }


    internal class ResimCompressAsyncTask(fragment: Fragment) : AsyncTask<String, String, String>() {
        var myFragment = fragment
        var compressFragment = YukleniyorFragment()

        ///.execute yazınca ilk bura sonra doın sonra onPostEx calısıyor.."2"
        override fun onPreExecute() {

            compressFragment.show(myFragment.activity!!.supportFragmentManager, "compress dıalog basladı")
            compressFragment.isCancelable = false

            super.onPreExecute()
        }


        override fun doInBackground(vararg params: String?): String {
            var yeniOlusanDosyaKlasor = File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/TestKlasor/compressed/") // sıkısan dosyanın kaydedılecegı yer."1"
            //yeniDosyayolu bize kaydedilen yolu veriyor.
            var yeniDosyaYolu = SiliCompressor.with(myFragment.context).compress(params[0], yeniOlusanDosyaKlasor) //burada sıkıstırdık. ve ılgılı "1" dosyaya kaydettık

            compressFragment.dismiss()
            return yeniDosyaYolu
        }
        //yeni dosya returnu alttakı resulta donuyor cunku "2" de yazdıgı gıbı sıkıstırma bıttıgnde burası calısıyor.
        override fun onPostExecute(filePath: String?) {
            compressFragment.dismiss()
            Log.e("Hata","yenı dosyanın adı."+filePath)

            (myFragment as ShareNextFragment).uploadStoage(filePath)

            super.onPostExecute(filePath)
        }
    }
}
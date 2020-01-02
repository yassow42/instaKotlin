package com.creativeoffice.utils

import com.creativeoffice.Models.Users

class EventbusDataEvents {

    internal class KayitBilgileriniGonder(
        var telNo: String?,
        var email: String?,
        var verificationID: String?,
        var code: String?,
        var emailKayit: Boolean
    )

    internal class KullaniciBilgileriniGonder(var kullanici: Users?)

    internal class PaylasilacakResmiGonder(var dosyaYol:String?,var dosyaTuruResimMi:Boolean?)

}
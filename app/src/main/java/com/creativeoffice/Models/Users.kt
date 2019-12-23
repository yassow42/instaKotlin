package com.creativeoffice.Models

class Users {
    var email: String? = null
    var password: String? = null
    var user_name: String? = null
    var adi_soyadi: String? = null
    var phone_number: String? = null
    var email_phone_number: String? = null
    var user_id: String? = null


    constructor(
        email:String,
        email_phone_number: String?,
        password: String?,
        user_name: String?,
        user_id: String,
        adi_soyadi: String?,
        phone_number: String?

    ) {
        this.email=email
        this.password = password
        this.user_name = user_name
        this.user_id = user_id
        this.adi_soyadi = adi_soyadi
        this.phone_number = phone_number
        this.email_phone_number = email_phone_number
    }

    constructor() {}

}
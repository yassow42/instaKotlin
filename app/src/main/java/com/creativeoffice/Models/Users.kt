package com.creativeoffice.Models

class Users {
    var email: String? = null
    var password: String? = null
    var user_name: String? = null
    var adi_soyadi: String? = null
    var phone_number: String? = null
    var email_phone_number: String? = null

    constructor(
        email: String?,
        password: String?,
        user_name: String?,
        adi_soyadi: String?
    ) {
        this.email = email
        this.password = password
        this.user_name = user_name
        this.adi_soyadi = adi_soyadi
    }

    constructor(
        password: String?,
        user_name: String?,
        adi_soyadi: String?,
        phone_number: String?,
        email_phone_number: String?
    ) {
        this.password = password
        this.user_name = user_name
        this.adi_soyadi = adi_soyadi
        this.phone_number = phone_number
        this.email_phone_number = email_phone_number
    }

    constructor() {}

}
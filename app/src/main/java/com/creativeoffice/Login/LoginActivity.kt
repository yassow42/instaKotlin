package com.creativeoffice.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.creativeoffice.instakotlin.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

    }

    private fun init() {

        etEmailTelorUserName.addTextChangedListener(watcher)
        etSifre.addTextChangedListener(watcher)

        tvKaydol.setOnClickListener {
            var intent = Intent(
                this,
                RegisterActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


    }


    private var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (etEmailTelorUserName.text.toString().length >= 6 && etSifre.text.toString().length >= 6) {
                btnGirisYap.isEnabled = true
                btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity,R.color.beyaz))
                btnGirisYap.setBackgroundResource(R.drawable.register_button_aktif)

            }else{

                btnGirisYap.isEnabled = false
                btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity,R.color.sonukmavi))
                btnGirisYap.setBackgroundResource(R.drawable.register_button)
            }
        }

    }
}

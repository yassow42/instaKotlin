package com.creativeoffice.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.creativeoffice.Home.HomeActivity
import com.creativeoffice.Models.Users
import com.creativeoffice.instakotlin.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var mRef: FirebaseDatabase
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mRef = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        init()
        setupAuthListener()

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

        btnGirisYap.setOnClickListener {

            oturumAcacakKullaniciyiDenetle(
                etEmailTelorUserName.text.toString(),
                etSifre.text.toString()
            )


        }
    }

    private fun oturumAcacakKullaniciyiDenetle(etEmailTelorUsername: String, sifre: String) {

        mRef.reference.child("users").orderByChild("email")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.getValue() != null) {
                        var kullaniciBulundu = false
                        for (ds in p0.children) {  //kullanıcıları okuduk.

                            var okunanKullanici = ds.getValue(Users::class.java)

                            if (okunanKullanici!!.email!!.toString().equals(etEmailTelorUsername)) {
                                oturumAc(okunanKullanici, sifre, false)
                                kullaniciBulundu = true
                                break

                            } else if (okunanKullanici!!.user_name!!.toString().equals(
                                    etEmailTelorUsername
                                )
                            ) {
                                oturumAc(
                                    okunanKullanici,
                                    sifre,
                                    false
                                ) // telefon no ile girmediği için false duryor ıkı tarafta
                                kullaniciBulundu = true
                                break

                            } else if (okunanKullanici!!.phone_number!!.toString().equals(
                                    etEmailTelorUsername
                                )
                            ) {
                                oturumAc(
                                    okunanKullanici,
                                    sifre,
                                    true
                                ) //burada tel no ile giriş yapmak istediginden true yaptık.
                                kullaniciBulundu = true
                                break
                            }

                        }
                        if (kullaniciBulundu == false) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Kullanıcı bulunamadı",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }
                }
            })

    }

    private fun oturumAc(okunanKullanici: Users, sifre: String, telefonİleGiris: Boolean) {
        var girisYapacakEmail = ""
        if (telefonİleGiris == true) {
            girisYapacakEmail = okunanKullanici.email_phone_number.toString()
        } else {
            girisYapacakEmail = okunanKullanici.email.toString()
            if (girisYapacakEmail == ""){
                girisYapacakEmail = okunanKullanici.email_phone_number.toString()
            }
        }

        mAuth.signInWithEmailAndPassword(girisYapacakEmail, sifre)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {
                    if (p0.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Oturum Açıldı   " + mAuth.currentUser!!.uid,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Şifre Hatalı ", Toast.LENGTH_LONG)
                            .show()

                    }
                }

            })


    }

    private var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (etEmailTelorUserName.text.toString().length >= 6 && etSifre.text.toString().length >= 6) {
                btnGirisYap.isEnabled = true
                btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.beyaz))
                btnGirisYap.setBackgroundResource(R.drawable.register_button_aktif)

            } else {

                btnGirisYap.isEnabled = false
                btnGirisYap.setTextColor(
                    ContextCompat.getColor(
                        this@LoginActivity,
                        R.color.sonukmavi
                    )
                )
                btnGirisYap.setBackgroundResource(R.drawable.register_button)
            }
        }

    }

    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    var intent = Intent(this@LoginActivity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                } else {

                }


            }

        }
    }
    override fun onStart() {
        mAuth.addAuthStateListener(mAuthListener)
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }


}

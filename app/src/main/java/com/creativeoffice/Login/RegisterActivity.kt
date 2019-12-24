package com.creativeoffice.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.creativeoffice.Models.Users
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.EventbusDataEvents
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    lateinit var manager: FragmentManager
    lateinit var mRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        manager = supportFragmentManager
        mRef = FirebaseDatabase.getInstance().reference


        manager.addOnBackStackChangedListener(this)
        init()
    }

    private fun init() {


        tvTelefon.setOnClickListener {

            viewTelefon.visibility = View.VISIBLE
            viewEposta.visibility = View.GONE

            etGirisYontemi.hint = "Telefon"
            etGirisYontemi.setText("")
            etGirisYontemi.inputType = InputType.TYPE_CLASS_NUMBER

            btnİleri.isEnabled = false
            btnİleri.setBackgroundResource(R.drawable.register_button)
            btnİleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.sonukmavi))
        }

        tvEposta.setOnClickListener {

            viewTelefon.visibility = View.GONE
            viewEposta.visibility = View.VISIBLE

            etGirisYontemi.hint = "E-Posta"
            etGirisYontemi.setText("")
            etGirisYontemi.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            btnİleri.isEnabled = false
            btnİleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.sonukmavi))
            btnİleri.setBackgroundResource(R.drawable.register_button)
        }


        etGirisYontemi.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {


                if (p0!!.length >= 10) {

                    btnİleri.isEnabled = true

                    btnİleri.setTextColor(
                        ContextCompat.getColor(
                            this@RegisterActivity,
                            R.color.beyaz
                        )
                    )
                    btnİleri.setBackgroundResource(R.drawable.register_button_aktif)

                } else {
                    btnİleri.isEnabled = false

                    btnİleri.setTextColor(
                        ContextCompat.getColor(
                            this@RegisterActivity,
                            R.color.sonukmavi
                        )
                    )
                    btnİleri.setBackgroundResource(R.drawable.register_button)


                }
            }


        })



        btnİleri.setOnClickListener {

            if (etGirisYontemi.hint.toString().equals("Telefon")) {

////////////////isValidTelefon metodunda bir telefon nuamrasına benzıyor mu ? diye kontrol saglıyoruz. //////////////////
                if (isValidTelefon(etGirisYontemi.text.toString())) {

                    var cepTelefonuKullanimdaMi = false
                    mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0.getValue() != null) {

                                for (user in p0.children) {

                                    var okunanKullanici = user.getValue(Users::class.java)

                                    if (okunanKullanici!!.phone_number!!.equals(etGirisYontemi.text.toString())) {


                                        Toast.makeText(this@RegisterActivity, "Telefon No Kullanımda", Toast.LENGTH_LONG).show()

                                        cepTelefonuKullanimdaMi = true
                                        break
                                    } else {
                                        cepTelefonuKullanimdaMi = false

                                    }


                                }

                                if (cepTelefonuKullanimdaMi == false) {
                                    loginRoot.visibility = View.GONE
                                    loginContainer.visibility = View.VISIBLE

                                    val transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(
                                        R.id.loginContainer,
                                        TelefonKoduGirFragment()
                                    )
                                    transaction.addToBackStack("TelefonKodu")
                                    transaction.commit()

                                    EventBus.getDefault().postSticky(
                                        EventbusDataEvents.KayitBilgileriniGonder(
                                            etGirisYontemi.text.toString(),
                                            null,
                                            null,
                                            null,
                                            false
                                        )
                                    )

                                }

                            }

                        }


                    })


                } else {
                    Toast.makeText(this, "Lutfen dogru bir tel no girin", Toast.LENGTH_LONG).show()
                }


            } else {


                if (isValidEmail(etGirisYontemi.text.toString())) {
                    pbRegister.visibility = View.VISIBLE
                    var emailKullanimdami = false

                    mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {


                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.getValue() != null) { //herhangi bir veri varsa çalıştırır.

                                for (user in p0.children) {

                                    var okunanKullanici = user.getValue(Users::class.java)

                                    if (okunanKullanici!!.email!!.equals(etGirisYontemi.text.toString())) {

                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "Email kullanımda",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        pbRegister.visibility = View.INVISIBLE

                                        emailKullanimdami = true
                                        break
                                    }
                                }
                                if (emailKullanimdami == false) {
                                    pbRegister.visibility = View.INVISIBLE
                                    loginRoot.visibility = View.GONE
                                    loginContainer.visibility = View.VISIBLE

                                    val transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.loginContainer, KayitFragment())
                                    transaction.addToBackStack("Email Giris")
                                    transaction.commit()


                                    EventBus.getDefault().postSticky(
                                        EventbusDataEvents.KayitBilgileriniGonder(
                                            null,
                                            etGirisYontemi.text.toString(),
                                            null,
                                            null,
                                            true
                                        )
                                    )

                                }

                            }

                        }

                    })


                } else {
                    Toast.makeText(this, "Lutfen gecerli bir mail girin", Toast.LENGTH_LONG).show()
                }


            }

        }

        tvGirisYap.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

    }


    override fun onBackStackChanged() {// fragmentlerde geri nasıl gelebılırz duzgunce bunu kullanmalıyız. ilk olarak lateinit manager tanımladık daha sonra oncreat içinde cagırdık
        // metodu acıp kullanıyruz . tek tek geri gelirken sayıyor eger. sıfır olursa da login root devrede login contaier ınvısıble

        val elemansayisi = manager.backStackEntryCount
        if (elemansayisi == 0) {
            loginRoot.visibility = View.VISIBLE
        }
    }

    fun isValidEmail(kontrolEdilecekMail: String): Boolean {

        if (kontrolEdilecekMail == null) {

            return false
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekMail).matches()
    }

    fun isValidTelefon(kontrolEdilecekTelefon: String): Boolean {

        if (kontrolEdilecekTelefon == null || kontrolEdilecekTelefon.length > 14) {

            return false
        }

        return android.util.Patterns.PHONE.matcher(kontrolEdilecekTelefon).matches()
    }
}

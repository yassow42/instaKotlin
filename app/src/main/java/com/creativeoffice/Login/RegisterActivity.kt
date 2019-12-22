package com.creativeoffice.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.EventbusDataEvents
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

   lateinit var manager : FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        manager = supportFragmentManager
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


                if (start + before + count >= 10) {

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

                loginRoot.visibility = View.GONE
                loginContainer.visibility = View.VISIBLE

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, TelefonKoduGirFragment())
                transaction.addToBackStack("TelefonKodu")
                transaction.commit()

                EventBus.getDefault().postSticky(EventbusDataEvents.TelefonNoGonder(etGirisYontemi.text.toString()))



            } else {

                loginRoot.visibility = View.GONE
                loginContainer.visibility = View.VISIBLE

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, KayitFragment())
                transaction.addToBackStack("Email Giris ")
                transaction.commit()


                EventBus.getDefault().postSticky(EventbusDataEvents.EmailGonder(etGirisYontemi.text.toString()))

            }

        }


    }



    override fun onBackStackChanged() {// fragmentlerde geri nasıl gelebılırz duzgunce bunu kullanmalıyız. ilk olarak lateinit manager tanımladık daha sonra oncreat içinde cagırdık
        // metodu acıp kullanıyruz . tek tek geri gelirken sayıyor eger. sıfır olursa da login root devrede login contaier ınvısıble

        val elemansayisi = manager.backStackEntryCount
        if (elemansayisi == 0){
            loginRoot.visibility = View.VISIBLE
        }


    }


}

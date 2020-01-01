package com.creativeoffice.Share

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.creativeoffice.Login.LoginActivity
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.BottomnavigationViewHelper
import com.creativeoffice.utils.SharePagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_share.*
import java.util.jar.Manifest

class ShareActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 2
    private val TAG = "ShareActivity"

    lateinit var mAuthListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupAuthListener()
        strageVeKameraİzniİste()
        // setupShareViewPager()


    }

    private fun strageVeKameraİzniİste() {

        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        setupShareViewPager()
                        Log.e("Hata", "izinler tamam")
                    }

                    if (report!!.isAnyPermissionPermanentlyDenied) {
                        Log.e("Hata", " İzinleri engellenmiş pic")

                        ///////////////////////////////alet dialogla izni tekrar istiyoru /////////////////////////////
                        var builder = AlertDialog.Builder(this@ShareActivity)
                        builder.setTitle("İzin Gerekli")
                        builder.setMessage("Ayarlar kısmından uygulamaya izin vermeniz gerekiyor.")
                        builder.setPositiveButton("Ayarlara git", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.cancel()
                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri = Uri.fromParts("package", packageName, null)
                                intent.setData(uri)
                                startActivity(intent)
                                finish()
                            }

                        })
                        builder.setNegativeButton("IPTAL", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.cancel()
                                finish()
                            }

                        })
                        builder.show()
                        ///////////////////////////////alet dialogla izni tekrar istiyoru /////////////////////////////
                    }

                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    ///izinlerden biri reddedildiğinde bu kısma dusuyor.
                    Log.e("Hata", " İzinlerden biri reddedilmiş")


                    ///////////////////////////////alet dialogla izni tekrar istiyoru /////////////////////////////
                    var builder = AlertDialog.Builder(this@ShareActivity)
                    builder.setTitle("İzin Gerekli")
                    builder.setMessage("Uygulamaya izin vermeniz gerekiyor. Onaylar mısınız?")
                    builder.setPositiveButton("Onay ver", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            token!!.continuePermissionRequest() // engellenen izni tekrar gosterıyor.
                            dialog!!.cancel()
                        }

                    })
                    builder.setNegativeButton("IPTAL", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            token!!.cancelPermissionRequest() //izin istemeyi bırakıyor.
                            dialog!!.cancel()
                            finish()
                        }

                    })
                    builder.show()
                    ///////////////////////////////alet dialogla izni tekrar istiyoru /////////////////////////////

                }

            })
            .withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(error: DexterError?) {
                    Log.e("Hata", error!!.toString())
                }

            })
            .check()

    }

    private fun setupShareViewPager() {
        var tabAdlari = ArrayList<String>()
        tabAdlari.add("Galeri")
        tabAdlari.add("FOTOĞRAF")
        tabAdlari.add("VİDEO")


        var sharePagerAdapter = SharePagerAdapter(supportFragmentManager, tabAdlari)

        sharePagerAdapter.addFragment(ShareGalleryFragment())
        sharePagerAdapter.addFragment(ShareCameraFragment())
        sharePagerAdapter.addFragment(ShareVideoFragment())

        shareViewPager.adapter = sharePagerAdapter

        shareTabLayout.setupWithViewPager(shareViewPager)

    }

    override fun onBackPressed() {

        anaLayout.visibility = View.VISIBLE
        fragmentContainerLayout.visibility = View.GONE


        super.onBackPressed()
    }

    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    var intent = Intent(this@ShareActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                } else {

                }


            }

        }
    }

}

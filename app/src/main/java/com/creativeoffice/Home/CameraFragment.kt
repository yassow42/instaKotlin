package com.creativeoffice.Home

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creativeoffice.Share.ShareNextFragment
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.DosyaIslemleri
import com.creativeoffice.utils.EventbusDataEvents
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.Gesture
import com.otaliastudios.cameraview.GestureAction
import kotlinx.android.synthetic.main.fragment_camera.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream

class CameraFragment : Fragment() {
    lateinit var cameraView: CameraView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { //saveInstance ise uygulama yan dondugunde hersey sıl bastan yapılır bunu engeller verileri korur. ınflater java kodlarını xml e cevırır. 
        var view = inflater.inflate(R.layout.fragment_camera, container, false) //biz fragmenti nereye koyarsak container orasıdır.

        cameraView = view.FotoCameraViewHome
        cameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM) //zoom ozeligi actık
        cameraView.mapGesture(Gesture.TAP, GestureAction.FOCUS) //focusozeligi actık
        // cameraView.setLifecycleOwner(viewLifecycleOwner)


        view.imgFotoCek.setOnClickListener {
            cameraView.capturePicture()
            Log.e("kameraHome", "foto cekildi")
        }


        view.constraintLayoutRoot.visibility= View.VISIBLE
        view.frameLayoutContainerHomeCamera.visibility=View.GONE

        cameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)
                var cekilenFotoAdi = System.currentTimeMillis()
                var cekilenFotoYolu = File((Environment.getExternalStorageDirectory()).absolutePath + "/DCIM/Camera/" + cekilenFotoAdi + ".jpg")
                var dosyaOlustur = FileOutputStream(cekilenFotoYolu)

                dosyaOlustur.write(jpeg)
                dosyaOlustur.close()

                view.constraintLayoutRoot.visibility= View.GONE
                view.frameLayoutContainerHomeCamera.visibility=View.VISIBLE

                var transaction= activity!!.supportFragmentManager.beginTransaction()
                /////////////////
                EventBus.getDefault().postSticky(EventbusDataEvents.PaylasilacakResmiGonder(cekilenFotoYolu.absolutePath.toString(),true))
                /////////////////
                transaction.addToBackStack("camera")
                transaction.replace(R.id.frameLayoutContainerHomeCamera,ShareNextFragment())
                transaction.commit()

            }
        })

        return view
    }



    override fun onResume() {

        super.onResume()
        Log.e("kameraHome", "foto Kamera calıstı")
        cameraView.start()

    }

    override fun onPause() {
        super.onPause()
        Log.e("kameraHome", "foto Kamera durdu")

        cameraView.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("kameraHome", "foto Kamera öldü")
        if (cameraView != null) {
            cameraView.destroy()
        }

    }

}
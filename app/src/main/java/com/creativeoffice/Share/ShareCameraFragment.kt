package com.creativeoffice.Share


import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.creativeoffice.instakotlin.R
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Mode
import kotlinx.android.synthetic.main.fragment_share_camera.*
import kotlinx.android.synthetic.main.fragment_share_camera.view.*

/**
 * A simple [Fragment] subclass.
 */
class ShareCameraFragment : Fragment() {
    lateinit var cameraView: CameraView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var view = inflater.inflate(R.layout.fragment_share_camera, container, false)

        cameraView = view.FotoCameraView

        cameraView.setLifecycleOwner(viewLifecycleOwner)

        view.imgFotoCek.setOnClickListener {

            cameraView.takePicture()
            Log.e("kamera", "foto cekildi")

        }

        return view
    }

    override fun onResume() {

        super.onResume()
        Log.e("kamera", "foto Kamera calıstı")
        cameraView.open()

    }

    override fun onPause() {
        super.onPause()
        Log.e("kamera", "foto Kamera durdu")

        cameraView.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("kamera", "foto Kamera öldü")

        cameraView.destroy()
    }

}

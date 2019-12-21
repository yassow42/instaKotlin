package com.creativeoffice.Profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar


import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*


class ProfileEditFragment : Fragment() {
    lateinit var circleProfileImageFragment: CircleImageView
    lateinit var mProgresBarFragment: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)

        mProgresBarFragment = view.findViewById(R.id.mProgresBar)
        circleProfileImageFragment = view.findViewById(R.id.circleProfileImage)

        setupProfilePicture()

        view.imgCLose.setOnClickListener {

            activity?.onBackPressed()

        }
        return view
    }


    private fun setupProfilePicture() {

        // https://smartpro.com.tr/wp-content/uploads/2019/05/android.jpg


        var imgURL = "i.imgyukle.com/2019/12/21/R6ekHS.jpg"
        var ilkKisim = "https://"

        UniversalImageLoader.setImage(
            imgURL,
            circleProfileImageFragment,
            mProgresBarFragment,
            ilkKisim
        )

    }


}

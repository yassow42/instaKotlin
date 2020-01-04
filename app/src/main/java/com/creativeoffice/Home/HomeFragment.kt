package com.creativeoffice.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creativeoffice.instakotlin.R
import com.creativeoffice.utils.BottomnavigationViewHelper
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    lateinit var fragmentView: View
    private val ACTIVITY_NO = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { //saveInstance ise uygulama yan dondugunde hersey sıl bastan yapılır bunu engeller verileri korur. ınflater java kodlarını xml e cevırır.
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false) //biz fragmenti nereye koyarsak container orasıdır.


        return fragmentView
    }

    override fun onResume() {
        setupNavigationView()
        super.onResume()
    }

    fun setupNavigationView() {
        var fragmentNavView = fragmentView.bottomNavigationView

        BottomnavigationViewHelper.setupBottomNavigationView(fragmentNavView)
        BottomnavigationViewHelper.setupNavigation(activity!!,fragmentNavView) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...

        var menu =fragmentNavView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

}
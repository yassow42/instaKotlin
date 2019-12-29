package com.creativeoffice.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import javax.xml.parsers.FactoryConfigurationError

class SharePagerAdapter(fm: FragmentManager,tabAdlari:ArrayList<String>) : FragmentPagerAdapter(fm) {
    private var mFragmentLıst: ArrayList<Fragment> = ArrayList()
    private var mTabAdlari:ArrayList<String> = tabAdlari

    override fun getItem(position: Int): Fragment {

        return mFragmentLıst.get(position)
    }

    override fun getCount(): Int {
        return mFragmentLıst.size

    }

    fun addFragment(fragment: Fragment) {
        mFragmentLıst.add(fragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTabAdlari.get(position)
    }
}
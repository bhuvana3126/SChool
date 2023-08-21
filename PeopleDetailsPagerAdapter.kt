package com.wholesale.jewels.fauxiq.baheekhata.ui.customer

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.view.ViewGroup

class PeopleDetailsPagerAdapter : androidx.viewpager.widget.PagerAdapter() {

    private val views = ArrayList<View>()


    override fun getItemPosition(`object`: Any): Int {
        val index = views.indexOf(`object`)
        return if (index == -1)
            androidx.viewpager.widget.PagerAdapter.POSITION_NONE
        else
            index
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = views[position]
        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    @JvmOverloads
    fun addView(v: View, position: Int = views.size): Int {
        views.add(position, v)
        return position
    }

    fun removeView(pager: androidx.viewpager.widget.ViewPager, v: View): Int {
        return removeView(pager, views.indexOf(v))
    }

    private fun removeView(pager: androidx.viewpager.widget.ViewPager, position: Int): Int {
        pager.adapter = null
        views.removeAt(position)
        pager.adapter = this

        return position
    }

    fun getView(position: Int): View {
        return views[position]
    }


}
package com.wholesale.jewels.fauxiq.baheekhata.ui.customer

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.dto.ToolbarContent
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.People
import kotlinx.android.synthetic.main.fragments_peopledetails_viewpager.*

class PeopleDetailsViewPager : androidx.fragment.app.Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewPagerAdapter: PeopleDetailsPagerAdapter
    private lateinit var people: People


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            people = it.getParcelable(PEOPLE_DETAILS) ?: People()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragments_peopledetails_viewpager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager(people)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val toolbarContent = ViewModelProviders.of(activity!!).get(ToolbarContent::class.java)
        toolbarContent.currentTitle.value = CLASS_SIMPLE_NAME
        toolbarContent.showOptionsMenu.value = false
        toolbarContent.showToolbarButton.value = false
    }

    private fun setViewPager(people: People) {

       // viewPagerAdapter = PeopleDetailsPagerAdapter(people)

        viewpager_people_details.adapter = viewPagerAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
      //  viewPagerAdapter.onDetach()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onBackPressed()
    }

    companion object {
        const val CLASS_SIMPLE_NAME: String = "People Details"
        const val PEOPLE_DETAILS: String = "People Details"

        @JvmStatic
        fun newInstance(people: People) = PeopleDetailsViewPager().apply {
            arguments = Bundle().apply {
                putParcelable(PEOPLE_DETAILS,people)
            }
        }
    }
}

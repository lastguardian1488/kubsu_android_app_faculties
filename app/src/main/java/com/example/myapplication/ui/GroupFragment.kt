package com.example.myapplication.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.data.Faculty
import com.example.myapplication.data.Group
import com.example.myapplication.data.Student
import com.example.myapplication.databinding.FragmentGroupBinding
import com.example.myapplication.models.GroupViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

const val GROUP_TAG = "GroupFragment"

class GroupFragment private constructor(): Fragment() {

    private var _binding: FragmentGroupBinding? = null

    private val binding
        get() = _binding!!

    companion object {
        private var id: Long = -1
        private var _group: Group? = null
        fun newInstance(id: Long): GroupFragment {
            this.id = id
            return GroupFragment()
        }

        val getFacultyId
            get() = id
        val getGroup
            get() = _group
    }

    private lateinit var viewModel: GroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GroupViewModel::class.java]
        viewModel.setFaculty(getFacultyId)

        CoroutineScope(Dispatchers.Main).launch {
            val f = viewModel.getFaculty()
            callbacks?.setTitle(f?.name ?: "UNKNOWN")
        }

        viewModel.faculty.observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private var tabPosition: Int = 0

    private fun updateUI(groups: List<Group>) {
        binding.tabGroup.clearOnTabSelectedListeners()
        binding.tabGroup.removeAllTabs()
        binding.faBtnNewStudent.visibility = if ((groups.size ?: 0) > 0) {
            binding.faBtnNewStudent.setOnClickListener {
//                callbacks?.showStudent(faculty?.groups!![tabPosition].id, null)
            }
            View.VISIBLE
        } else
            View.GONE
        for (i in 0 until (groups.size ?: 0)) {
            binding.tabGroup.addTab(binding.tabGroup.newTab().apply {
                text = i.toString()
            })
        }

        val adapter = GroupPageAdapter(requireActivity(), groups)
        binding.vpGroup.adapter = adapter
        TabLayoutMediator(binding.tabGroup, binding.vpGroup, true, true) { tab, pos ->
            tab.text = groups.get(pos).name
        }.attach()

        binding.tabGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
                _group = groups[tabPosition]
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private inner class GroupPageAdapter(fa: FragmentActivity, private val groups: List<Group>) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return (groups.size ?: 0)
        }

        override fun createFragment(position: Int): Fragment {
            return GroupListFragment(groups.get(position))
        }
    }

    interface Callbacks {
        fun setTitle(_title: String)
        fun showStudent(groupID: UUID, student: Student?)
    }

    var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }
}
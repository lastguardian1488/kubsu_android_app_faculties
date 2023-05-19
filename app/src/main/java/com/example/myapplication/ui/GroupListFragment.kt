package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Group
import com.example.myapplication.data.Student
import com.example.myapplication.databinding.FragmentGroupListBinding
import com.example.myapplication.models.GroupListViewModel
import java.util.*

class GroupListFragment(private val group : List<Student>?) : Fragment() {
    private lateinit var viewModel: GroupListViewModel
    private var _binding: FragmentGroupListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvGroupList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
        binding.rvGroupList.adapter = GroupListAdapter(group ?: emptyList())
    }

    private inner class GroupHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var student: Student

        fun bind(student: Student) {
            this.student = student
            val s = "${student.lastName} ${student.firstName!![0]}. ${student.middleName!![0]}."
            itemView.findViewById<TextView>(R.id.tvElement).text = s
            itemView.findViewById<ConstraintLayout>(R.id.studButtons).visibility = View.GONE
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val cl = itemView.findViewById<ConstraintLayout>(R.id.studButtons)
            cl.visibility = View.VISIBLE
            lastItemView?.findViewById<ConstraintLayout>(R.id.studButtons)?.visibility = View.GONE
            lastItemView = if (lastItemView == itemView) null else itemView
            if (cl.visibility == View.VISIBLE) {
                itemView.findViewById<ImageButton>(R.id.studDelBtn).setOnClickListener {
                    commitDeleteDialog(student)
                }
                itemView.findViewById<ImageButton>(R.id.studEditBtn).setOnClickListener {
                    callbacks?.showStudent(student.groupID!!, student)
                }
            }
        }
    }

    private fun commitDeleteDialog(student: Student) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Удалить студента ${student.lastName} ${student.firstName} ${student.middleName} из списка?")
        builder.setTitle("Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
//            viewModel.deleteStudent(group.id, student)
        }
        builder.show()
    }

    private var lastItemView: View? = null

    private inner class GroupListAdapter(private val items: List<Student>) :
        RecyclerView.Adapter<GroupListFragment.GroupHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): GroupHolder {
            val view = layoutInflater.inflate(R.layout.fragment_student_list_element, parent, false)
            return GroupHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: GroupHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    interface Callbacks {
        fun showStudent(groupID: Long, student: Student?)
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
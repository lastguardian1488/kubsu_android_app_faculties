package com.example.myapplication.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.data.Student
import com.example.myapplication.databinding.FragmentStudentBinding
import com.example.myapplication.models.StudentViewModel
import java.util.*

const val STUDENT_TAG="StudentFragment"

class StudentFragment : Fragment() {
    private lateinit var viewModel: StudentViewModel
    private var _binding: FragmentStudentBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        lateinit var groupID: UUID
        var student: Student? = null
        fun newInstance(groupID: UUID, student: Student?): StudentFragment {
            this.student = student
            this.groupID = groupID
            return StudentFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[StudentViewModel::class.java]

        if (student != null) {
            binding.etFirstName.setText(student!!.firstName)
            binding.etLastName.setText(student!!.lastName)
            binding.etMiddleName.setText(student!!.middleName)
            binding.etPhone.setText(student!!.phone)
            val dt = GregorianCalendar().apply {
                time = student!!.birthDate
            }
            binding.dpCalendar.init(
                dt.get(Calendar.YEAR),
                dt.get(Calendar.MONTH),
                dt.get(Calendar.DAY_OF_MONTH),
                null
            )
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showCommitDialog()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun showCommitDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setCancelable(true)
        builder.setMessage("Сохранить изменения?")
        builder.setTitle("Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            var p = true
            binding.etFirstName.text.toString().ifBlank {
                p = false
                binding.etFirstName.error = "Укажите значение"
            }
            binding.etLastName.text.toString().ifBlank {
                p = false
                binding.etLastName.error = "Укажите значение"
            }
            binding.etMiddleName.text.toString().ifBlank {
                p = false
                binding.etMiddleName.error = "Укажите значение"
            }
            binding.etPhone.text.toString().ifBlank {
                p = false
                binding.etPhone.error = "Укажите значение"
            }
            if (GregorianCalendar().get(GregorianCalendar.YEAR) - binding.dpCalendar.year < 2) {
                p = false
                Toast.makeText(context, "Укажите правильно возраст", Toast.LENGTH_LONG).show()
            }
            if (p) {
                val selectedDate = GregorianCalendar().apply {
                    set(GregorianCalendar.YEAR, binding.dpCalendar.year)
                    set(GregorianCalendar.MONTH, binding.dpCalendar.month)
                    set(GregorianCalendar.DAY_OF_MONTH, binding.dpCalendar.dayOfMonth)
                }
                if (student == null) {
                    student = Student()
                    student?.apply {
                        firstName = binding.etFirstName.text.toString()
                        lastName = binding.etLastName.text.toString()
                        middleName = binding.etMiddleName.text.toString()
                        phone = binding.etPhone.text.toString()
                        birthDate = selectedDate.time
                    }
                    viewModel.newStudent(groupID!!, student!!)
                } else {
                    student?.apply {
                        firstName = binding.etFirstName.text.toString()
                        lastName = binding.etLastName.text.toString()
                        middleName = binding.etMiddleName.text.toString()
                        phone = binding.etPhone.text.toString()
                        birthDate = selectedDate.time
                    }
                }
                backPressedCallback.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            backPressedCallback.isEnabled = false
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val alert = builder.create()
        alert.show()
    }
}
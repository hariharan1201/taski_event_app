package com.practise.taski_event.frags.eventdialog.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.practise.taski_event.R
import com.practise.taski_event.base.BaseAppFragment
import com.practise.taski_event.repo.AppState
import com.practise.taski_event.data.Event
import com.practise.taski_event.databinding.FragmentAddEventBinding
import com.practise.taski_event.frags.eventdialog.viewmodel.AddEventViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class AddEventEventFragment : BaseAppFragment() {

    private val viewModel: AddEventViewModel by viewModels()
    private lateinit var binding: FragmentAddEventBinding
    private val args: AddEventEventFragmentArgs by navArgs()
    private var mode = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            mode = args.event == null

            if(mode){
                getString(R.string.add_event).apply {
                    backBtn.text = this
                    addEvent.text = this
                }
            }
            else{
                getString(R.string.edit_event).apply { backBtn.text = this }
                addEvent.text = getString(R.string.update_event)
                args.event?.let { bindData(it) }
            }

            viewModel.appState.observe(viewLifecycleOwner) {state ->
                if(state == AppState.Loading)
                    showProgress()
                else
                    hideProgress()
                when(state){
                    AppState.Success -> {
                        findNavController().popBackStack()
                        showToast(if(mode) getString(R.string.event_added) else getString(R.string.event_updated))
                    }
                    else -> {}
                }
            }

            backBtn.setOnClickListener { findNavController().popBackStack() }

            dateFld.setOnClickListener {
                dateInputLayout.error = null
                hideKeyboard(requireView())
                showDatePicker()
            }

            timeFld.setOnClickListener {
                timeInputLayout.error = null
                val date = dateFld.text.toString()
                hideKeyboard(requireView())
                if (date.isNotBlank()) {
                    showTimePickerWithDateCheck(date)
                } else {
                    showToast(getString(R.string.please_choose_a_date_first))
                }
            }

            addEvent.setOnClickListener {
                val isTitleValid = titleInputLayout.validateRequired(getString(R.string.title_required))
                val isDescriptionValid = descriptionInputLayout.validateRequired(getString(R.string.description_required))
                val isDateValid = dateInputLayout.validateRequired(getString(R.string.date_required))
                val isTimeValid = timeInputLayout.validateRequired(getString(R.string.time_required))

                if(isTimeValid && isDateValid && isTitleValid && isDescriptionValid) {
                    if(mode){
                        viewModel.addEvent(
                            Event(
                                id = 0,
                                title = titleFld.text.toString(),
                                description = descriptionFld.text.toString(),
                                date = dateFld.text.toString(),
                                time =  timeFld.text.toString()
                            )
                        )
                    }else{
                        viewModel.addEvent(
                            Event(
                                id = args.event!!.id,
                                title = titleFld.text.toString(),
                                description = descriptionFld.text.toString(),
                                date = dateFld.text.toString(),
                                time =  timeFld.text.toString()
                            )
                        )
                    }
                }
            }

        }
    }

    private fun TextInputLayout.validateRequired(errorMessage: String): Boolean {
        val isValid = this.editText?.text?.isNotBlank() == true
        this.error = if (isValid) null else errorMessage
        return isValid
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                binding.dateFld.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = calendar.timeInMillis
        datePicker.show()
    }

    private fun showTimePickerWithDateCheck(selectedDate: String) {
        val now = Calendar.getInstance()

        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)
        val selectedDateCal = Calendar.getInstance().apply {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            time = sdf.parse(selectedDate) ?: now.time
        }

        val isToday = now.get(Calendar.YEAR) == selectedDateCal.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == selectedDateCal.get(Calendar.DAY_OF_YEAR)

        val timePicker = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                if (isToday && !selectedTime.after(now)) {
                    Toast.makeText(requireContext(), "Please choose a future time", Toast.LENGTH_SHORT).show()
                } else {
                    val amPm = if (selectedHour >= 12) "PM" else "AM"
                    val hour12 = if (selectedHour == 0 || selectedHour == 12) 12 else selectedHour % 12
                    val formatted = String.format("%02d:%02d %s", hour12, selectedMinute, amPm)
                    binding.timeFld.setText(formatted)
                }
            },
            hour,
            minute,
            false
        )

        timePicker.show()
    }

    private fun bindData(event: Event) {
        binding.apply {
           titleFld.setText(event.title)
           descriptionFld.setText(event.description)
           timeFld.setText(event.time)
           dateFld.setText(event.date)
        }
    }


}
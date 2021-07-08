package com.example.list.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.list.Extensions.format
import com.example.list.Extensions.text
import com.example.list.databinding.ActivityAddTaskBinding
import com.example.list.datasource.TaskDataSource
import com.example.list.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity () {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)){
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
            }
        }

        insertListener()

    }

    private fun insertListener () {

    binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val TimeZone = TimeZone.getDefault()
                val offset = TimeZone.getOffset(Date().time)* -1
    binding.tilDate.editText?.setText(Date(it + offset).format())

    }

    datePicker.show(supportFragmentManager,"DATE_PICKER_TAG")

    }

    binding.tilHour.editText?.setOnClickListener {
        val timePicker = MaterialTimePicker.Builder()

            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

    timePicker.addOnPositiveButtonClickListener {
    val minute = if (timePicker.minute in 0..9)"0${timePicker.minute}" else timePicker.minute

    val hour = if(timePicker.hour in 0..9)"0${timePicker.hour}" else timePicker.hour

        binding.tilHour.text = "$hour:$minute"

    }

    timePicker.show(supportFragmentManager, null)

    }

    binding.btnCancel.setOnClickListener {
         finish()
    }

    binding.btnNewTask.setOnClickListener {
        val task = Task(

            title =binding.tilTitle.text,
            date = binding.tilDate.text,
            hour = binding.tilHour.text,
            id = intent.getIntExtra(TASK_ID,0)
        )
    TaskDataSource.insertTask(task)
    setResult(Activity.RESULT_OK)
    finish()
    }
  }
    companion object {
        const val TASK_ID = "task_id"
    }
}
package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var addCourseViewModel: AddCourseViewModel
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_course)

        val factory = AddCourseViewModelFactory.createInstance(this)
        addCourseViewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        addCourseViewModel.saved.observe(this) {
            if (it.getContentIfNotHandled() == true)
                onBackPressed()
            else {
                Toast.makeText(this, "Data ada yang kosong", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val addNameCourse = findViewById<TextInputEditText>(R.id.add_name_course).text.toString().trim()
                val addSpinnerDay = findViewById<Spinner>(R.id.add_spinner_day).selectedItemPosition
                val addTextStartTime = findViewById<TextView>(R.id.add_text_start_time).text.toString().trim()
                val addTextEndTime = findViewById<TextView>(R.id.add_text_end_time).text.toString().trim()
                val addNameLecturer = findViewById<TextInputEditText>(R.id.add_name_lecturer).text.toString().trim()
                val addNote = findViewById<TextInputEditText>(R.id.add_note).text.toString().trim()

                addCourseViewModel.insertCourse(addNameCourse, addSpinnerDay, addTextStartTime, addTextEndTime, addNameLecturer, addNote)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun showStartTimePicker(view: View) {
        TimePickerFragment().show(
            supportFragmentManager, " startTime"
        )
        this.view = view
    }

    fun showEndTimePicker(view: View) {
        TimePickerFragment().show(
            supportFragmentManager, " endTime"
        )
        this.view = view
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (view.id) {
            R.id.add_start_time -> {
                findViewById<TextView>(R.id.add_text_start_time).text = timeFormat.format(calender.time)
            }
            R.id.add_end_time -> {
                findViewById<TextView>(R.id.add_text_end_time).text = timeFormat.format(calender.time)
            }
        }
    }

}
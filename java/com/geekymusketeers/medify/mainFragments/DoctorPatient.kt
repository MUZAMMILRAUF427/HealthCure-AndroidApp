package com.geekymusketeers.medify.mainFragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medify.databinding.ActivityDoctorPatientBinding
import com.geekymusketeers.medify.adapter.DoctorsAppointmentAdapter
import com.geekymusketeers.medify.appointment.DoctorAppointment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DoctorPatient : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorPatientBinding
    private lateinit var dbref: Query
    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentAdapter: DoctorsAppointmentAdapter
    private lateinit var appointmentList: ArrayList<DoctorAppointment>
    private lateinit var sharedPreference: SharedPreferences

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        appointmentList = ArrayList()
        appointmentAdapter = DoctorsAppointmentAdapter(appointmentList)

        recyclerView = binding.appointmentRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        recyclerView.setHasFixedSize(true)

        val userID = sharedPreference.getString("uid", "Not found").toString()
        val date: StringBuilder = StringBuilder(intent.getStringExtra("date").orEmpty())

        if (date.isNotEmpty()) {
            val hide = intent.getStringExtra("hide")
            if (hide == "hide") binding.selectDate.visibility = View.INVISIBLE

            val doctorIntentUid = intent.getStringExtra("uid").toString()
            getData(date.toString(), doctorIntentUid)
        }

        binding.selectDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val tempDate = dateFormatter.format(Date(it)).trim()
                date.setLength(0)
                date.append(tempDate)
                appointmentList.clear()
                binding.selectDate.text = date
                getData(date.toString(), userID)
            }

            datePicker.addOnNegativeButtonClickListener {
                Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
            }

            datePicker.addOnCancelListener {
                Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(date: String, userID: String) {
        dbref = FirebaseDatabase.getInstance()
            .getReference("Doctor")
            .child(userID)
            .child("DoctorsAppointments")
            .child(date)
            .orderByChild("TotalPoints")

        dbref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                appointmentList.clear()
                if (snapshot.exists()) {
                    for (appointmentSnapshot in snapshot.children) {
                        val appointment = appointmentSnapshot.getValue(DoctorAppointment::class.java)
                        if (appointment != null) {
                            appointmentList.add(appointment)
                            Log.d("TotalPoints", "${appointment.TotalPoints} ${appointment.PatientName}")
                        }
                    }
                    appointmentList.sortByDescending { it.TotalPoints }
                    recyclerView.adapter = appointmentAdapter
                    Log.d("User", appointmentList.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.selectDateTextToHide.visibility = View.GONE
    }
}

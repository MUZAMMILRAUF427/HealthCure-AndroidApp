package com.geekymusketeers.medify.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medify.databinding.ActivitySignUpFirstBinding

class SignUp_First : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.nextButton.setOnClickListener {
            val ageText = binding.ageInput.text.toString().trim()
            val isDoctor = binding.stickySwitch.getText()

            if (ageText.isEmpty()) {
                Toast.makeText(baseContext, "Enter age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null) {
                Toast.makeText(baseContext, "Enter a valid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isDoctor.equals("Doctor", ignoreCase = true) && age < 23) {
                Toast.makeText(baseContext, "23 is the minimum age of a Doctor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SignUp_Activity::class.java).apply {
                putExtra("isDoctor", isDoctor.toString())
                putExtra("age", age.toString())
            }
            startActivity(intent)
        }
    }
}

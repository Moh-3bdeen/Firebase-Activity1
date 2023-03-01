package com.msa2002.firebaseactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.msa2002.firebaseactivity.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore
    var birtdate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val today = Calendar.getInstance()
        binding.dpBirthdate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            birtdate = "$day/${month + 1}/$year"
        }

        binding.btnSave.setOnClickListener {
            val idText = binding.etId.text.toString()
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()

            if (idText.isEmpty() || name.isEmpty() || email.isEmpty() || birtdate.isEmpty()){
                Toast.makeText(applicationContext, "Fill all fields !", Toast.LENGTH_SHORT).show()
            }else{
                try {
                    val id = idText.toInt()
                    addStudent(id, name, email, birtdate)
                } catch (ex: Exception){
                    Toast.makeText(applicationContext, "Error, ${ex.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addStudent(id: Int, name: String, email: String, birthdate: String){
        val student = hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "birthdate" to birthdate
        )

        db.collection("Students")
            .add(student)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(applicationContext, "Student has been added", Toast.LENGTH_SHORT).show()
                binding.etId.text?.clear()
                binding.etName.text?.clear()
                binding.etEmail.text?.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Add failed, ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
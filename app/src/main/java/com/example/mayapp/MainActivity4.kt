package com.example.mayapp

import android.R
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import com.example.mayapp.databinding.ActivityMain4Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class MainActivity4 : AppCompatActivity() {
    private lateinit var binding: ActivityMain4Binding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var spinner: Spinner
    private var previousSelectedItem: String? = null  // Variable to track previous selected item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        spinner = binding.spinner


        binding.open1.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://youtu.be/5ZV_88_C9Rc?si=nsPUJDZEFAscX6PK")
            )
            startActivity(intent)
        }

        binding.open2.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91 7030435112"))
            startActivity(intent)
        }

        binding.open3.setOnClickListener {
            val intent = Intent(Intent.ACTION_POWER_USAGE_SUMMARY)
            startActivity(intent)
        }

        binding.viewInfo.setOnClickListener {
            val name = binding.Name.text.toString().trim()
            val reg = binding.Carno.text.toString().trim()
            val branch = binding.carcomp.text.toString().trim()

            if (name.isNotEmpty() && reg.isNotEmpty() && branch.isNotEmpty()) {
                val intent = Intent(this, MainActivity5::class.java).apply {
                    putExtra("name", name)
                    putExtra("Regno", reg)
                    putExtra("branch", branch)
                }
                startActivity(intent)
                finish()
            } else {
                val set = AnimatorSet()
                val mTarget = findViewById<TextView>(R.id.button3)
                val bounceAnim = ObjectAnimator.ofFloat(mTarget, "translationX", 0f, 100f, 0f)
                bounceAnim.duration = 100
                set.playTogether(bounceAnim)
                set.start()
                set.setDuration(90)
                set.start()
                MotionToast.createColorToast(
                    this, "Please fill all the details!", "Fill the correct details",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helveticabold)
                )


            }
            val ownername = binding.Name.text.toString()
            val carid = binding.Carno.text.toString()
            val carbrand = binding.carcomp.text.toString()
            val ownermob = binding.mono.text.toString()

            if(ownername.isEmpty() || carid.isEmpty() || carbrand.isEmpty() || ownermob.isEmpty() ||spinner.isNotEmpty() ) {
                FancyToast.makeText(
                    this, "Please fill all details!",
                    FancyToast.LENGTH_LONG,
                    FancyToast.WARNING, false
                ).show()
            }else {


                // saving the user entered values in the realtime database
                //for that create a data class, as here Userdata is our dataclass
                //Below is the code for saving data from mobile to database

                val currentuser = auth.currentUser
                    currentuser?. let { user ->
                        val notekey = databaseReference.child("users").child(user.uid).child("notes").push().key

                        // Getting the data

                        val Data = Userdata(ownername, carid, carbrand,ownermob)
                        if(notekey != null) {
                            // filling the data in created database
                            databaseReference.child("users").child(user.uid).child("notes").child(notekey).setValue(Data)
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful) {
                                        FancyToast.makeText(
                                            this, "Information Saved",
                                            FancyToast.LENGTH_LONG,
                                            FancyToast.SUCCESS, false
                                        ).show()

                                    }
                                    else {
                                        FancyToast.makeText(
                                            this, "Failed to Save Information",
                                            FancyToast.LENGTH_LONG,
                                            FancyToast.ERROR, false
                                        ).show()

                                    }
                                    }
                        }


                }
            }

        }

        // spinner


        val spinnerlist = listOf("Diesel Vehicle", "Petrol Vehicle")
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, spinnerlist)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        // Set item selected listener for the spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selitem = parent?.getItemAtPosition(position).toString()

                // Check if the selected item is different from the previous item
                if (selitem != previousSelectedItem) {
                    // Show the toast with the selected item

                }

                // Update previous selected item
                previousSelectedItem = selitem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when no item is selected (optional)
                Toast.makeText(this@MainActivity4, "No item selected!", Toast.LENGTH_SHORT).show()
            }
        }




    }
}
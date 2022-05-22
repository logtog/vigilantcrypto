package com.logtog.vigilantcrypto.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.logtog.vigilantcrypto.data.database.RealTimeDatabase
import com.logtog.vigilantcrypto.data.model.User
import com.logtog.vigilantcrypto.databinding.ActivitySignUpBinding

@SuppressLint("StaticFieldLeak")
private lateinit var binding : ActivitySignUpBinding
private lateinit var firebaseAuth : FirebaseAuth
private lateinit var database : DatabaseReference

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = RealTimeDatabase().getDataBaseRealtimeUser()

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupBtn.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.edEmail.text.toString()
            val phone = binding.edCelular.text.toString()
            val cpf = binding.edCpf.text.toString()
            val birthday = binding.edNasc.text.toString()
            val password = binding.edPassword.text.toString()
            val confirmPassword = binding.edConfirmPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && cpf.isNotEmpty()
                && birthday.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){

                            val uid = firebaseAuth.currentUser?.uid

                            val user = User(name, email, phone, cpf, birthday, password)

                            database.child(uid.toString()).setValue(user).addOnSuccessListener {
                                Toast.makeText(this, "Success, Sign up!", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener{
                                Toast.makeText(this, "Failed to save credentials", Toast.LENGTH_SHORT).show()
                            }

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this,"Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Field Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
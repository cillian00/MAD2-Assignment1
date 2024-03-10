package org.wit.gym.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import org.wit.placemark.R

class Register : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailTextField = findViewById<EditText>(R.id.email)
        val passwordTextField = findViewById<EditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.register)
        val progressBar = findViewById<ProgressBar>(R.id.progress)
        val textToLogin = findViewById<TextView>(R.id.click_to_login)

        textToLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }


        registerButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = emailTextField.text.toString().trim()
            val password = passwordTextField.text.toString().trim()
            val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

            if (email.isEmpty() || password.isEmpty() || !email.matches(emailPattern.toRegex())) {
                progressBar.visibility = View.GONE
                Snackbar.make(it, "Email must match regex and password/email cannot be empty", Snackbar.LENGTH_LONG).show()
            } else {
                progressBar.visibility = View.GONE
                Snackbar.make(it, "Registration successful", Snackbar.LENGTH_LONG).show()

                Log.i(email, "Email there boyo")
                Log.i(password, "Password there boyo")

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }




                emailTextField.text.clear()
                passwordTextField.text.clear()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {

        Log.i(user.toString(), "GotEm")
    }
}

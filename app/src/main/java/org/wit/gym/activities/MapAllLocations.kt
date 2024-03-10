package org.wit.gym.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import org.wit.placemark.R

class MapAllLocations : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.activity_login)
        val emailTextField = findViewById<EditText>(R.id.email)
        val passwordTextField = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val progressBar = findViewById<ProgressBar>(R.id.progress)
        val textToLogin = findViewById<TextView>(R.id.click_to_register)

        textToLogin.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = emailTextField.text.toString().trim()
            val password = passwordTextField.text.toString().trim()
            val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

            if (email.isEmpty() || password.isEmpty() || !email.matches(emailPattern.toRegex())) {
                progressBar.visibility = View.GONE
                Snackbar.make(
                    it,
                    "Email must match regex and password/email cannot be empty",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                progressBar.visibility = View.GONE
                Snackbar.make(it, "Registration successful", Snackbar.LENGTH_LONG).show()

                Log.i(email, "Email there boyo")
                Log.i(password, "Password there boyo")

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "createUserWithEmail:success")
                            val user = auth.currentUser

                            val intent = Intent(this, GymListActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
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
}
package com.example.logggg

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var emailEditText: TextInputEditText? = null
    private var passwordEditText: TextInputEditText? = null
    private var emailInputLayout: TextInputLayout? = null
    private var passwordInputLayout: TextInputLayout? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        emailInputLayout = findViewById(R.id.email_input_layout)
        passwordInputLayout = findViewById(R.id.password_input_layout)
        val loginButton = findViewById<Button>(R.id.login_button)
        val signupLink = findViewById<TextView>(R.id.signup_link)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Set up button click listener
        loginButton.setOnClickListener { v: View? ->
            if (validateInputs()) {
                loginUser()
            }
        }

        // Set up signup link click listener
        signupLink.setOnClickListener { v: View? ->
            val intent1 = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent1)
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun validateInputs(): Boolean {
        val email = emailEditText!!.text.toString().trim { it <= ' ' }
        val password = passwordEditText!!.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout!!.error = "Invalid email address"
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            passwordInputLayout!!.error = "Password must be at least 6 characters"
            return false
        }
        return true
    }

    private fun loginUser() {
        val email = emailEditText!!.text.toString().trim { it <= ' ' }
        val password = passwordEditText!!.text.toString().trim { it <= ' ' }

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = mAuth!!.currentUser
                    if (user != null) {
                        // Navigate to the home page
                        val intent = Intent(this@LoginActivity, Base_Activity::class.java)
                        startActivity(intent)
                        finish()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    emailInputLayout!!.error =
                        "Authentication failed. Please check your credentials."
                }
            }
    }
}

package com.example.logggg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : Activity() {
    private var emailEditText: TextInputEditText? = null
    private var passwordEditText: TextInputEditText? = null
    private var confirmPasswordEditText: TextInputEditText? = null
    private var passwordInputLayout: TextInputLayout? = null
    private var confirmPasswordInputLayout: TextInputLayout? = null
    private var passwordStrengthTextView: TextView? = null
    private var signupButton: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var loginlink: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize views
        emailEditText = findViewById(R.id.signup_email)
        passwordEditText = findViewById(R.id.signup_password)
        confirmPasswordEditText = findViewById(R.id.signup_confirm_password)
        passwordStrengthTextView = findViewById(R.id.password_strength)
        signupButton = findViewById(R.id.signup_button)
        loginlink = findViewById(R.id.login_link)

        passwordInputLayout = findViewById(R.id.signup_password_input_layout)
        confirmPasswordInputLayout = findViewById(R.id.signup_confirm_password_input_layout)

        // Password strength indicator
        passwordEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updatePasswordStrength(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // Signup button click listener
        signupButton?.setOnClickListener { v: View? ->
            if (validateInputs()) {
                val email = emailEditText?.text.toString().trim()
                val password = passwordEditText?.text.toString().trim()

                mAuth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            Snackbar.make(
                                v ?: return@OnCompleteListener,
                                "Registration successful! Welcome, $email. You can now log in with your email and password.",
                                Snackbar.LENGTH_LONG
                            ).show()
                            val currentUser = mAuth?.currentUser
                            // Navigate to login screen and pass email and password
                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            intent.putExtra("email", email)
                            intent.putExtra("password", password)
                            startActivity(intent)
                            finish() // Finish the signup activity so it can't be returned to
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Snackbar.make(
                                v ?: return@OnCompleteListener,
                                "Registration failed. Please try again.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }

        loginlink?.setOnClickListener {
            val intent1 = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent1)
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth?.currentUser
        if (currentUser != null) {
            Utils.showSuccessToast(this, "You are already signed in.")
        }
    }

    // Validate user inputs
    private fun validateInputs(): Boolean {
        var isValid = true

        val email = emailEditText?.text.toString().trim()
        val password = passwordEditText?.text.toString().trim()
        val confirmPassword = confirmPasswordEditText?.text.toString().trim()

        if (email.isEmpty()) {
            emailEditText?.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText?.error = "Please enter a valid email"
            isValid = false
        }

        if (password.isEmpty()) {
            passwordEditText?.error = "Password is required"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText?.error = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordEditText?.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    // Update password strength indicator
    private fun updatePasswordStrength(password: String) {
        var strength = "Weak"
        if (password.length >= 8 && password.matches(".*\\d.*".toRegex()) && password.matches(".*[a-zA-Z].*".toRegex())) {
            strength = "Medium"
        }
        if (password.length >= 12 && password.matches(".*\\d.*".toRegex()) && password.matches(".*[a-z].*".toRegex()) && password.matches(".*[A-Z].*".toRegex()) && password.matches(".*[!@#\\$%^&*].*".toRegex())) {
            strength = "Strong"
        }

        passwordStrengthTextView?.text = "Password Strength: $strength"
    }

    companion object {
        private const val TAG = "SignupActivity"
    }
}

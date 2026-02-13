package com.portes.it342mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoRegister = findViewById<Button>(R.id.btnGoRegister)

        val tokenManager = TokenManager(this)

        // 1. SESSION CHECK: If already logged in, skip to Dashboard
        lifecycleScope.launch {
            tokenManager.getToken().collect { token ->
                if (!token.isNullOrEmpty()) {
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                }
            }
        }

        // 2. NAVIGATION: Go to Register screen
        btnGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 3. LOGIN LOGIC
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password)
            RetrofitClient.instance.login(loginRequest)
                .enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        val body = response.body()

                        if (response.isSuccessful && body?.status == "success") {
                            val token = body.token

                            // Start a coroutine to handle saving and navigation
                            lifecycleScope.launch {
                                if (!token.isNullOrEmpty()) {
                                    // Suspend until token is actually saved
                                    tokenManager.saveToken(token)
                                }

                                // Now that we are sure the token is saved, navigate.
                                // This prevents Dashboard from kicking the user back to Login.
                                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                                finish()
                            }
                        } else {
                            // Display the error message from Java backend (e.g., "Invalid email or password")
                            val errorMsg = body?.message ?: "Login failed"
                            Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
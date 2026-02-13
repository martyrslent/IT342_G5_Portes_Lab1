package com.portes.it342mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        tokenManager = TokenManager(this)

        // Protect the dashboard: redirect to login if no token
        lifecycleScope.launch {
            tokenManager.getToken().collect { token ->
                if (token.isNullOrEmpty()) {
                    startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                    finish()
                } else {
                    // Optionally, show a welcome message with token info
                    tvWelcome.text = "Welcome!"
                }
            }
        }

        btnLogout.setOnClickListener {
            // Call backend logout API
            RetrofitClient.instance.logout().enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    Toast.makeText(this@DashboardActivity, "Logged out", Toast.LENGTH_SHORT).show()

                    // Clear token locally
                    lifecycleScope.launch {
                        tokenManager.clearToken()
                        startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                        finish()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    Toast.makeText(this@DashboardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

package com.dicoding.batiksnapapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.dicoding.batiksnapapplication.data.repositor.Result
import androidx.lifecycle.ViewModelProvider
import com.dicoding.batiksnapapplication.data.LoginResponse
import com.dicoding.batiksnapapplication.data.Preference
import com.dicoding.batiksnapapplication.databinding.ActivityMainBinding
import com.dicoding.batiksnapapplication.ui.HomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory(this)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)


        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if(!PatternsCompat.EMAIL_ADDRESS.matcher(s).matches()){
                    binding.emailEditText.error ="Email harus mengandung karakter @gmail.com"
                } else {
                    binding.emailEditText.error = null
                }
            }
        })



        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            if (!email.contains("@")) {
                binding.edLoginEmail.error = "Email harus mengandung karakter @gmail.com"
                return@setOnClickListener
            }

            if (password.length < 8) {
                binding.edLoginPassword.error = "Password harus memiliki 8 karakter"
                return@setOnClickListener
            }


            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = "Masukkan email"

                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = "Masukkan password"
                }
                else -> {
                    mainViewModel.login(email, password).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    processLogin(result.data)
                                    showLoading(false)
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }
        }
        OnBackPressed()
    }


    private fun processLogin(data: LoginResponse) {
        if (data.error) {
            Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
        } else {
            Preference.saveToken(data.access_token, this)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun showLoading(state: Boolean) {
        binding.apply {
            pbLogin.isVisible = state
            edLoginEmail.isInvisible = state
            emailTextView.isInvisible = state
            passwordTextView.isInvisible = state
            messageTextView.isInvisible = state
            edLoginPassword.isInvisible = state
            btnLogin.isInvisible = state
            buttonRegister.isInvisible = state
            tvRegister.isInvisible = state
        }
    }


    private fun OnBackPressed() {
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
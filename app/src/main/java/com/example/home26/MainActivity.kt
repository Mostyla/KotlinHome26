package com.example.home26

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.home26.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Autorization"

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        viewModel.getResponse().observe(this) {
            handleResponse(it)
        }

        binding.enterBtn.setOnClickListener {
            Log.d("tttt","click!")
            val userName = binding.loginEt.text.toString()
            val userPassword = binding.passwordEt.text.toString()
            viewModel.sendRequest(userName, userPassword)
        }

    }

    private fun handleResponse(response: String) {
        if (response.contains("ok")) {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                this@MainActivity,
                "Авторизация прошла успешно!",
                Toast.LENGTH_LONG
            ).show()
            saveInfo()
        } else {
            Toast.makeText(
                this@MainActivity,
                "Неверное имя пользователя или пароль!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun saveInfo() {
        if (binding.sw.isChecked) {
            val editor = sharedPrefs.edit()
            editor.putString("STATUS", "SUCCESS").apply()
        }
    }

    override fun onStart() {
        super.onStart()
        sharedPrefs = getSharedPreferences("SAVE", MODE_PRIVATE)
        val status = sharedPrefs.getString("STATUS", "NULL")
        if (status == "SUCCESS")
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
    }
}
// login: Maskit99 pasw: !Makaka322
package com.example.home26

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.home26.ApiConst.APIKEY
import com.example.home26.ApiConst.APISECRETKEY
import com.example.home26.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.enterBtn.setOnClickListener {
            sendRequest(binding.loginEt.text.toString(), binding.passwordEt.text.toString())
        }

    }

    private fun sendRequest(login: String, password: String) {
        val apiSignature =
            "api_key" + APIKEY + "methodauth.getMobileSession" + "password" + password + "username" + login + APISECRETKEY
        Thread {
            val hexString = StringBuilder()

            // Create MD5 Hash
            val digest = MessageDigest
                .getInstance("MD5")
            digest.update(apiSignature.toByteArray(charset("UTF-8")))
            val messageDigest = digest.digest()

            // Create Hex String
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            val urlParameter =
                "method=auth.getMobileSession&api_key=$APIKEY&password=$password&username=$login&api_sig=$hexString"
            val request = Request.Builder()
                .url("https://ws.audioscrobbler.com/2.0/?$urlParameter")
                .post(RequestBody.create(null, ByteArray(0))).build()
            val client = OkHttpClient.Builder().build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    response.body().use { responseBody ->
                        var test = responseBody!!.string()
                        Log.d("MyLog", test)
                        if (test.contains("ok")) {
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Авторизация прошла успешно!", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            runOnUiThread {
                                    Toast.makeText(this@MainActivity, "Неверное имя пользователя или пароль!", Toast.LENGTH_LONG).show()
                            }
                        }
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    }
                }
            })
        }.start()

    }
}
// login: Maskit99 pasw: !Makaka322
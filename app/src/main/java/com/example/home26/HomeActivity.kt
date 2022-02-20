package com.example.home26

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home26.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var audioAdapter: AudioRcViewAdapter
    private lateinit var viewModel: MyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "AudioList"
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        setUpRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit)
            sharedPrefs = getSharedPreferences("SAVE", MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.remove("STATUS").apply()
        val intent = (Intent(this@HomeActivity, MainActivity::class.java))
        startActivity(intent)
        return true
    }

    private fun setUpRecyclerView() {
        viewModel.load()
        audioAdapter = AudioRcViewAdapter()
        viewModel.audioLiveData.observe(this) {
            audioAdapter.audio = it
        }
        binding.recyclerTracks.layoutManager = LinearLayoutManager(this@HomeActivity)
        binding.recyclerTracks.adapter = audioAdapter
    }
}
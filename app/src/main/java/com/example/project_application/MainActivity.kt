package com.example.project_application

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_speaker.setOnClickListener {
            val intent = Intent(this, SmartSpeakers::class.java)
            startActivity(intent)
        }
        btn_led.setOnClickListener {
            val intent = Intent(this, Led2::class.java)
            startActivity(intent)
        }
        btn_fire.setOnClickListener {
            val intent = Intent(this, FireCaution::class.java)
            startActivity(intent)
        }

        btn_lock.setOnClickListener {
            val intent = Intent(this, PhoneLock::class.java)
            startActivity(intent)
        }

        btn_cctv.setOnClickListener {
            val intent = Intent(this, Cctv::class.java)
            startActivity(intent)
        }
    }
}
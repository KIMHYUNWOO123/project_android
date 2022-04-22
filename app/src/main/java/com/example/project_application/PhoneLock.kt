package com.example.project_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_phone_lock.*
import org.eclipse.paho.client.mqttv3.MqttMessage

const val LOCK_PUB_TOPIC = "iot/phonelock"

class PhoneLock : AppCompatActivity() {
    lateinit var mqttClient: Mqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_lock)

        mqttClient = Mqtt(this, SERVICE_URI)

        try {
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>(LOCK_PUB_TOPIC))
        } catch(e: Exception){
            e.printStackTrace()
        }

        lockButton.setOnClickListener{
            val month : String = if (DatePicker.month <10){
                "0" + DatePicker.month.toString()
            } else{
                DatePicker.month.toString()
            }
            val day = if (DatePicker.dayOfMonth<10){
                "0" + DatePicker.month.toString()
            } else{
                DatePicker.dayOfMonth.toString()
            }
            val year : String = DatePicker.year.toString()
            val hour : String = if (timePicker.hour < 10){
                "0" + timePicker.hour.toString()
            } else{
                timePicker.hour.toString()
            }
            val minute : String = if(timePicker.minute < 10){
                "0" + timePicker.minute.toString()
            } else {
                timePicker.minute.toString()
            }

            val dateandtime = year + "/" + month + "/" + day + "@" + hour + ":" + minute

            mqttClient.publish(LOCK_PUB_TOPIC, dateandtime)
            Log.i("Dateandtime", "$dateandtime")
        }

    }

    fun onReceived(topic:String, message: MqttMessage) {
        val msg = String(message.payload)
        Log.i("Mqtt", "$msg")
    }
}
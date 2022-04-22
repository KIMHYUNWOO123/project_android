package com.example.project_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_home_led.*
import org.eclipse.paho.client.mqttv3.MqttMessage

const val TOPIC_MYROOM = "iot/light/myRoom"
const val TOPIC_KITCHEN = "iot/light/kitchen"
const val TOPIC_LIVING = "iot/light/livingRoom"

class HomeLed : AppCompatActivity() {
    lateinit var mqttClient: Mqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_led)

        mqttClient = Mqtt(this, SERVICE_URI)

        livingLED.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                mqttClient.publish(TOPIC_LIVING, "livingroom turn on")
            }else{
                mqttClient.publish(TOPIC_LIVING, "livingroom turn off")
            }
        }

        kitchenLed.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                mqttClient.publish(TOPIC_KITCHEN, "kitchen turn on")
            }else{
                mqttClient.publish(TOPIC_KITCHEN, "kitchen turn off")
            }
        }

        myroonLed.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                mqttClient.publish(TOPIC_MYROOM, "myroom turn on")
            }else{
                mqttClient.publish(TOPIC_MYROOM, "myroom turn off")
            }
        }
    }
}
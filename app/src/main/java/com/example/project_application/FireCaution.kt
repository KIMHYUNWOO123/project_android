package com.example.project_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_fire_caution.*
import org.eclipse.paho.client.mqttv3.MqttMessage

const val SUB_TOPIC2 = "iot/fire"
const val SERVICE_URI2 = "tcp://172.30.1.49:1883" //raspberryPi's IP.

class FireCaution : AppCompatActivity() {
    val TAG = "MqttActivity"
    lateinit var mqttClient: Mqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_caution)
        mqttClient = Mqtt(this, SERVICE_URI2)

        try {
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>(SUB_TOPIC2))
        } catch(e: Exception){
            e.printStackTrace()
        }

    }
    fun onReceived(topic:String, message: MqttMessage) {
        val msg = String(message.payload)
        Log.i("Mqtt", "$msg")

        if(msg == "No Fire!!"){
            textView2.text = "No Fire Detected"
        }

        else{
            textView2.text = "Fire!!"
            var builder = NotificationCompat.Builder(this, "MY_channel")
                .setContentTitle("스마트홈 알림")
                .setContentText("집에 불이 났습니다.")
        }
    }

}
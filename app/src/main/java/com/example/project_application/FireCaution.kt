package com.example.project_application

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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

        if (textView2.text == "Fire!!"){
            fireButton.visibility = View.VISIBLE
            fireButton.setOnClickListener{
                var intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:119")
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
            }
        }else{
            fireButton.visibility = View.INVISIBLE
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

        }
    }

}
package com.example.project_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_led2.*
import kotlinx.android.synthetic.main.activity_led2.kitchenLed
import kotlinx.android.synthetic.main.activity_led2.livingLED
import kotlinx.android.synthetic.main.activity_led2.myroonLed
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.internal.wire.MqttReceivedMessage

const val SUB_TOPIC = "iot/#"
const val PUB_TOPIC2 = "iot/light"
const val TOPIC_MYROOM = "iot/light/myRoom"
const val TOPIC_KITCHEN = "iot/light/kitchen"
const val TOPIC_LIVING = "iot/light/livingRoom"
const val SERVICE_URI = "tcp://172.30.1.33"

class Led2 : AppCompatActivity() {
    val TAG = "MqttActivity"
    lateinit var mqttClient: Mqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_led2)

        textView.text = light_value

        mqttClient = Mqtt(this, SERVICE_URI)

        try {
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>(SUB_TOPIC))
        } catch(e: Exception){
            e.printStackTrace()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textView.text = progress.toString()
                var lightvalue = progress.toString()
                light_value = lightvalue
                mqttClient.publish(PUB_TOPIC2, lightvalue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

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

    fun onReceived(topic:String, message: MqttMessage) {
        val msg = String(message.payload)
        Log.i("Mqtt", "$msg")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var progress = seekBar.progress
        outState.putInt("progress", progress)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val data = savedInstanceState.getInt("progress")

        seekBar.setProgress(data)
        textView.text = data.toString()
    }
}
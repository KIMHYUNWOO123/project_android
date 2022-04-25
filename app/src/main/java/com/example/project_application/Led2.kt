package com.example.project_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_led2.*
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.internal.wire.MqttReceivedMessage

const val SUB_TOPIC = "iot/#"
const val PUB_TOPIC2 = "iot/light"
const val SERVICE_URI = "tcp://172.30.1.33"

class Led2 : AppCompatActivity() {
    val TAG = "MqttActivity"
    lateinit var mqttClient: Mqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_led2)

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
                val lightvalue = progress.toString()
                mqttClient.publish(PUB_TOPIC2, lightvalue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
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
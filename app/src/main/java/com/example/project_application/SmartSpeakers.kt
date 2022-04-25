package com.example.project_application

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_smart_speakers.*
import org.eclipse.paho.client.mqttv3.*


class SmartSpeakers : AppCompatActivity() {
    val ServerIP:String = "tcp://192.168.0.3:1883"
    val TOPIC:String = "Mqtt"
    val TOPIC_PB:String = "Mqtt_pb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_speakers)

        var mqttClient: MqttClient? = null
        mqttClient = MqttClient(ServerIP, MqttClient.generateClientId(), null)
        mqttClient.connect()

        btn_weather.setOnClickListener {
            main_text.text = " "
            bitcoin_layout.visibility = View.INVISIBLE
            translation_layout.visibility = View.INVISIBLE
            Log.d("MQTTService", "Send")
            mqttClient.publish(TOPIC, MqttMessage("weather".toByteArray()))
        }
        btn_trans.setOnClickListener {
            main_text.text = " "
            bitcoin_layout.visibility = View.INVISIBLE
            translation_layout.visibility = View.VISIBLE
            Log.d("MQTTService", "Send")
        }
        btn_coin.setOnClickListener {
            bitcoin_layout.visibility = View.VISIBLE
            translation_layout.visibility = View.INVISIBLE
            Log.d("MQTTService", "Send")
        }
        btn_food.setOnClickListener {
            main_text.text = " "
            bitcoin_layout.visibility = View.INVISIBLE
            translation_layout.visibility = View.INVISIBLE
            Log.d("MQTTService", "Send")
            mqttClient.publish(TOPIC, MqttMessage("food".toByteArray()))
        }

        btn_btc.setOnClickListener {
            mqttClient.publish(TOPIC, MqttMessage("btc".toByteArray()))
        }
        btn_eth.setOnClickListener {
            mqttClient.publish(TOPIC, MqttMessage("eth".toByteArray()))
        }
        btn_sand.setOnClickListener {
            mqttClient.publish(TOPIC, MqttMessage("sand".toByteArray()))
        }
        btn_ltc.setOnClickListener {
            mqttClient.publish(TOPIC, MqttMessage("ltc".toByteArray()))
        }
        btn_xrp.setOnClickListener {
            mqttClient.publish(TOPIC, MqttMessage("xrp".toByteArray()))
        }
        btn_translate.setOnClickListener {
            var text = translate_text.text.toString()
            if (translate_text.text == null || translate_text.text == null) {
                return@setOnClickListener
            }
            if (translate_text.text.isEmpty() || translate_text.text.length == 0) {
                return@setOnClickListener
            }
            Log.d("DFD", text)
            text = "trans$text"
            mqttClient.publish(TOPIC, MqttMessage(text.toByteArray()))
        }

        mqttClient.subscribe(TOPIC_PB)
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d("MQTTService", "Connection")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d("MQTTService","Message Arrived : " + message.toString())
                val msg = message.toString()
                if (msg.contains("coin")) {
                    var msg1  = msg.split(",")
                    main_text.text = "현재 시세: " + msg1[1] + "원"
                }
                if (msg.contains("trans")) {
                    var msg1 = msg.split(",")
                    translated_text.text = msg1[1]
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d("MQTTService", "Delivery Complete")
            }
        })
    }
    fun onReceived(topic: String, message: MqttMessage) {
        val msg = String(message.payload)
        Log.i("Mqtt", "수신 메세지 :  $msg")
    }

}
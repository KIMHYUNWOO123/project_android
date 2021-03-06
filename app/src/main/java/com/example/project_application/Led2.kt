package com.example.project_application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.NotificationCompat
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
        seekBar.progress = seekBar_value
        livingLED.isChecked = livingLed_boolean
        kitchenLed.isChecked = KitchenLed_boolean
        myroonLed.isChecked = MyroonLed_boolean

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
                seekBar_value = lightvalue.toInt()
                mqttClient.publish(PUB_TOPIC2, lightvalue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        livingLED.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                livingLed_boolean = true
                mqttClient.publish(TOPIC_LIVING, "livingroom turn on")
                Led_Noti("ON", "??????")
            }else{
                livingLed_boolean = false
                mqttClient.publish(TOPIC_LIVING, "livingroom turn off")
                Led_Noti("OFF", "??????")
            }
        }

        kitchenLed.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                KitchenLed_boolean = true
                mqttClient.publish(TOPIC_KITCHEN, "kitchen turn on")
                Led_Noti("ON", "??????")


            }else{
                KitchenLed_boolean = false
                mqttClient.publish(TOPIC_KITCHEN, "kitchen turn off")
                Led_Noti("OFF", "??????")
            }
        }


        myroonLed.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                MyroonLed_boolean= true
                mqttClient.publish(TOPIC_MYROOM, "myroom turn on")
                Led_Noti("ON", "??? ???")
            }else{
                MyroonLed_boolean= false
                mqttClient.publish(TOPIC_MYROOM, "myroom turn off")
                Led_Noti("OFF", "??? ???")
            }
        }
    }
    fun Led_Noti(state:String, location:String) {
        Toast.makeText(this@Led2,"$location LED $state",Toast.LENGTH_SHORT).show()

        if (state == "ON") {
            var builder = NotificationCompat.Builder(this, "MY_channel")
                .setSmallIcon(R.drawable.ic_on)
                .setContentTitle("$location LED")
                .setContentText("$state")
                .setAutoCancel(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // ????????? ?????? ???????????? ????????? ?????? ??? ????????? ??????
                val channel_id = "MY_channel" // ????????? ?????? ?????? id ??????
                val channel_name = "????????????" // ?????? ?????? ??????
                val descriptionText = "?????????" // ?????? ????????? ??????
                val importance = NotificationManager.IMPORTANCE_DEFAULT // ?????? ???????????? ??????
                val channel = NotificationChannel(channel_id, channel_name, importance).apply {
                    description = descriptionText
                }

                // ?????? ?????? ????????? ???????????? ??????
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                // ?????? ??????: ????????? ?????? ID(ex: 1002), ?????? ??????
                notificationManager.notify(1002, builder.build())
            }
        }
        if (state == "OFF"){
            var builder = NotificationCompat.Builder(this, "MY_channel")
                .setSmallIcon(R.drawable.ic_off)
                .setContentTitle("$location LED")
                .setContentText("$state")
                .setAutoCancel(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // ????????? ?????? ???????????? ????????? ?????? ??? ????????? ??????
                val channel_id = "MY_channel" // ????????? ?????? ?????? id ??????
                val channel_name = "????????????" // ?????? ?????? ??????
                val descriptionText = "?????????" // ?????? ????????? ??????
                val importance = NotificationManager.IMPORTANCE_DEFAULT // ?????? ???????????? ??????
                val channel = NotificationChannel(channel_id, channel_name, importance).apply {
                    description = descriptionText
                }

                // ?????? ?????? ????????? ???????????? ??????
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                // ?????? ??????: ????????? ?????? ID(ex: 1002), ?????? ??????
                notificationManager.notify(1002, builder.build())
                }
            }
        }
    fun onReceived(topic:String, message: MqttMessage) {
        val msg = String(message.payload)
        Log.i("Mqtt", "$msg")
    }
}
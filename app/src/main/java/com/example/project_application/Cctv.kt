package com.example.project_application

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_application.Mqtt
import com.example.project_application.R
import kotlinx.android.synthetic.main.activity_cctv.*
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.client.mqttv3.MqttMessage


// Mqtt 토픽설정
const val CCTV_TOPIC ="iot/cctv" //Publish Topic from PC
const val CCTV_URI = "tcp://172.30.1.33" //Put PC IP Address
// tcp://이거 무조건 해야해요 안그러면 publish failed 됨 이유는 모름 -단희
// MQtt 토픽설정 끝

class Cctv : AppCompatActivity() {
    lateinit var mqttClient: Mqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cctv)

        mqttClient = Mqtt(this, CCTV_URI)

        try {
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>(CCTV_TOPIC))
        } catch(e: Exception){
            e.printStackTrace()
        }

// cctv 웹뷰
        webview.settings.javaScriptEnabled = true // 자바허용
        webview.webViewClient = WebViewClient() // 창 방지
        webview.webChromeClient = WebChromeClient() // 창 방지

        webview.loadUrl("http://172.30.1.49:8000/mjpeg/?mode=stream") // link 로드, IP 주소입력
// cctv 웹뷰끝
// Button Key value
        button_R.setOnClickListener{
//            KeyEvent.KEYCODE_R

            if(button_R.text == "녹화하기"){
                mqttClient.publish(CCTV_TOPIC, "r")
                Toast.makeText(this, "녹화가 시작되었습니다.", Toast.LENGTH_SHORT).show()

                button_R.text = "녹화종료하기"
            }else if(button_R.text == "녹화종료하기"){
                mqttClient.publish(CCTV_TOPIC, "rs")
                Toast.makeText(this, "녹화가 종료되었습니다..", Toast.LENGTH_SHORT).show()

                button_R.text = "녹화하기"
            }

        }
        button_Q.setOnClickListener {
//            KeyEvent.KEYCODE_Q
            mqttClient.publish(CCTV_TOPIC, "q")
            Toast.makeText(this, "녹화가 시작되었습니다..", Toast.LENGTH_SHORT).show()

        }
        button_C.setOnClickListener{
//            KeyEvent.KEYCODE_C
            mqttClient.publish(CCTV_TOPIC, "c")
            Toast.makeText(this, "캡처가 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }

        /*At RPi, subscribe topic "iot/cctv"

        import os

        ... python codes for mqtt subscribe

        def on_messsage(client, userdata, msg):
            order = str(msg.payload.decode("utf-8"))

            if order == "R":
                os.system("R") //cmd command. it's same with pushing "R" Button at RPi.

            elif order == "Q":
                os.system("Q")

            elif order == "C":
                os.system("C")

         Or

         def on_message(client, userdata, msg):
            order = str(msg.payload.decode("utf-8"))
            os.system(order)

         */


    }
    override fun onBackPressed() {
        if(webview.canGoBack())
        {
            webview.goBack() // 웹 사이트에서 뒤로 갈 페이지
        }
        else{
            super.onBackPressed()   // 본래 백버튼 기능
        }
    }
//  cctv 웹뷰 끝

//  Btn 설정 시작


    fun onReceived(topic:String, message: MqttMessage) {
        val msg = String(message.payload)
        Log.i("Mqtt", "$msg")
    }

}
//  Mqtt 설정

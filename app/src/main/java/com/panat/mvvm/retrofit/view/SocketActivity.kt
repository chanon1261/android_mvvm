package com.panat.mvvm.retrofit.view

import android.util.Log
import com.panat.mvvm.retrofit.MainApp
import com.panat.mvvm.retrofit.base.BaseActivity
import com.panat.mvvm.retrofit.databinding.ActivitySocketBinding
import com.panat.mvvm.retrofit.utils.AckWithTimeOut
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject


class SocketActivity : BaseActivity<ActivitySocketBinding>() {

    private lateinit var socket: Socket

    override fun initView() {
        bindView(ActivitySocketBinding.inflate(layoutInflater))

        openSocket()
        initSocket()

        binding.send.setOnClickListener {
            if (binding.message.text.toString().isNotEmpty()) {

                val obj = JSONObject()
                try {
                    obj.put("name", binding.message.text.toString())
                    obj.put("age", 20)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                val timeOut = 10000L
                socket.emit("news-ack", obj, object : AckWithTimeOut(timeOut) {
                    override fun call(vararg args: Any?) {
                        if (args[0].toString().equals("No Ack", true)) {
                            Log.d("ack_socket", "AckWithTimeOut : " + args[0].toString())
                        } else if (args[0].toString().equals("woot", true)) {
                            cancelTimer() //cancel timer if emit ACK return true
                            Log.d("ack_socket", "AckWithTimeOut : " + args[0].toString())
                        }
                    }
                })

                socket.emit("news", obj)
                binding.message.setText("")
            }
        }
        binding.message.setText("login")
        binding.send.performClick()


        socket.on("paper-ack") { args ->
            runOnUiThread {
                println("ack_data paper-news  ${args[0]}")
            }
        }

        socket.on("paper-ack") { args ->
            runOnUiThread {
                println("ack_data paper-news  ${args[0]}")
            }
        }

        socket.on("ping") { args ->
            runOnUiThread {
                println("ack_data ping  ${socket.id()} ")
            }
        }
    }


    private fun openSocket() {
        try {
            socket = IO.socket(MainApp.BaseUrl)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
        }
    }

    private fun initSocket() {
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        socket.on(Socket.EVENT_RECONNECT_FAILED, onConnectError)
        socket.on(Socket.EVENT_DISCONNECT, onConnectError)
        socket.on(Socket.EVENT_RECONNECT_ERROR, onConnectError)
        socket.on(Socket.EVENT_CONNECT) { }
        socket.on(Socket.EVENT_RECONNECT, onConnectError)
        socket.connect()
    }

    private var onConnectError = Emitter.Listener { args ->
        Log.e("onConnectError", "Exception " + args[0])
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.close()
        socket.disconnect()
    }
}

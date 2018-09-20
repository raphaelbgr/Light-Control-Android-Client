package br.edu.infnet.lightcontrol

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.fusesource.hawtbuf.Buffer
import org.fusesource.hawtbuf.UTF8Buffer
import org.fusesource.mqtt.client.*


class MainActivity : AppCompatActivity() {

    private lateinit var connection: CallbackConnection
    private lateinit var logAdapter: LogAdapter
    private lateinit var mqtt: MQTT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupLogRecycler()
    }

    private fun setupLogRecycler() {
        logAdapter = LogAdapter()
        text_log.setHasFixedSize(true)
        text_log.layoutManager = LinearLayoutManager(this)
        text_log.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        text_log.adapter = logAdapter
    }

    override fun onResume() {
        super.onResume()
        setupMqttClient()
        createMqttCallbackConnection()
        connectMqtt()
    }

    override fun onPause() {
        super.onPause()
        disconnectMqtt()
    }

    private fun setupMqttClient() {
        mqtt = MQTT()
        mqtt.setHost("iot.eclipse.org", 1883)
        mqtt.willQos = QoS.AT_LEAST_ONCE
    }

    private fun createMqttCallbackConnection() {
        connection = mqtt.callbackConnection()
        connection.listener(object : Listener {

            override fun onDisconnected() {
                text_mqtt_status.text = getString(R.string.offline)
                text_mqtt_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, android.R.color.holo_red_dark))
                text_raspi_status.text = getString(R.string.offline)
                text_raspi_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, android.R.color.holo_red_dark))
                appLog(getString(R.string.connection_dropped))
            }

            override fun onConnected() {
                text_mqtt_status.text = getString(R.string.online)
                text_mqtt_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, R.color.green))
                appLog("Conectado a iot.eclipse.org!")
            }

            override fun onPublish(topic: UTF8Buffer, payload: Buffer, ack: Runnable) {
                // You can now process a received message from a topic.
                // Once process execute the ack runnable.
                ack.run()
            }

            override fun onFailure(value: Throwable?) {
                text_mqtt_status.text = getString(R.string.offline)
                text_mqtt_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, android.R.color.holo_red_dark))
                text_raspi_status.text = getString(R.string.offline)
                text_raspi_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, android.R.color.holo_red_dark))
            }
        })
    }

    private fun connectMqtt() {
        connection.connect(object : Callback<Void> {
            override fun onFailure(value: Throwable?) {
                appLog("Falha na tentativa de conexão com iot.eclipse.org")
            }

            // Once we connect..
            override fun onSuccess(v: Void?) {
                appLog("Socket conectado!")
                subscribeToTopic()
            }
        })
    }

    private fun subscribeToTopic() {
        val topics = arrayOf(Topic("tcc_light_control_infnet", QoS.AT_LEAST_ONCE))
        connection.subscribe(topics, object : Callback<ByteArray> {
            override fun onSuccess(qoses: ByteArray?) {
                // The result of the subcribe request.
                appLog("Inscrito no tópico tcc_light_control_infnet!")
                searchRaspBerry()
            }

            override fun onFailure(value: Throwable?) {
                appLog("Falha na inscrição do tópico...")
            }
        })
    }

    private fun searchRaspBerry() {
        // Send a message to a topic
        connection.publish("tcc_light_control_infnet", "command_is_raspberry_alive".toByteArray(), QoS.AT_LEAST_ONCE, false, object : Callback<Void> {
            override fun onSuccess(v: Void?) {
                // the pubish operation completed successfully.
                appLog("Esperando RaspBerry Pi 3...")
            }

            override fun onFailure(value: Throwable?) {
//                connection.close(null) // publish failed.
            }
        })

    }

    private fun disconnectMqtt() {
        connection.disconnect(object : Callback<Void> {
            override fun onSuccess(v: Void?) {
                // called once the connection is disconnected.
            }

            override fun onFailure(value: Throwable?) {
                // Disconnects never fail.
            }
        })
    }

    private fun appLog(msg: String) {
        runOnUiThread {
            logAdapter.addLog(msg)
            text_log.post { text_log.smoothScrollToPosition(logAdapter.itemCount) }
        }
        Log.d("MQTT", msg)
    }
}

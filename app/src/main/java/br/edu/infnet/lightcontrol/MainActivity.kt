package br.edu.infnet.lightcontrol

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import br.edu.infnet.lightcontrol.model.ControlledLight
import br.edu.infnet.lightcontrol.model.Payload
import br.edu.infnet.lightcontrol.model.ServerResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.fusesource.hawtbuf.Buffer
import org.fusesource.hawtbuf.UTF8Buffer
import org.fusesource.mqtt.client.*


class MainActivity : AppCompatActivity(), PowerLightButtonClick {
    private lateinit var connection: CallbackConnection

    private lateinit var logAdapter: LogAdapter
    private lateinit var mqtt: MQTT
    private lateinit var adapter: LightControlAdapter

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
        appLog("Inicializando...")
        progress_view.visibility = View.VISIBLE
        light_list.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        setupMqttClient()
        createMqttCallbackConnection()
        connectMqtt()
    }

    override fun onPause() {
        super.onPause()
        progress_view.visibility = View.GONE
        light_list.isEnabled = true
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
            override fun onPublish(topic: UTF8Buffer, payload: Buffer, ack: Runnable) {
                ack.run()
                processCommand(payload.utf8().toString())
            }

            override fun onDisconnected() {
                text_mqtt_status.text = getString(R.string.offline)
                text_mqtt_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, android.R.color.holo_red_dark))
                text_raspi_status.text = getString(R.string.offline)
                text_raspi_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, android.R.color.holo_red_dark))
                appLog(getString(R.string.connection_dropped))
                runOnUiThread {
                    progress_view.visibility = View.GONE
                    light_list.isEnabled = true
                }
            }

            override fun onConnected() {
                text_mqtt_status.text = getString(R.string.online)
                text_mqtt_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, R.color.green))
                appLog("Conectado a iot.eclipse.org!")
                subscribeToTopic()
            }

            override fun onFailure(value: Throwable?) {
                text_mqtt_status.text = getString(R.string.offline)
                text_mqtt_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, android.R.color.holo_red_dark))
                text_raspi_status.text = getString(R.string.offline)
                text_raspi_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, android.R.color.holo_red_dark))
                runOnUiThread {
                    progress_view.visibility = View.GONE
                    light_list.isEnabled = false
                }
            }
        })
    }

    private fun processCommand(command: String) {
        when (command) {
            "raspberry_pi_im_alive" -> {
                setRaspBerryOnline()
                fetchBuilding(1)
            }
        }
        if (command.contains("fetch_building_id_response")) {
            appLog("Configurações recebidas!")
            val json = command.replace("fetch_building_id_response", "")
            val payload: Payload? = Gson().fromJson(json, ServerResponse::class.java)?.payload
            Log.d("JSON", payload.toString())
            if (payload != null) {
                runOnUiThread {
                    title = payload.name
                    setupLightControlRecycler(payload)
                    progress_view.visibility = View.GONE
                    light_list.isEnabled = true
                }
            }
        }
    }

    private fun setupLightControlRecycler(payload: Payload) {
        if (!::adapter.isInitialized) {
            adapter = LightControlAdapter()
            adapter.setPowerButtonClickListener(this)
            light_list.setHasFixedSize(true)
            light_list.adapter = adapter
        }
        adapter.clearArray()
        adapter.addToArray(payload)
    }

    private fun fetchBuilding(id: Int) {
        appLog("Requisitando configurações do condomínio...")
        runOnUiThread {
            progress_view.visibility = View.VISIBLE
            light_list.isEnabled = false
        }
        sendCommand("fetch_building_id_$id")
    }

    private fun setRaspBerryOnline() {
        text_raspi_status.text = getString(R.string.online)
        text_raspi_status.setTextColor(ContextCompat.getColor(text_mqtt_status.context, R.color.green))
        appLog("Raspberry Pi 3 Online!")
    }

    private fun connectMqtt() {
        connection.connect(object : Callback<Void> {
            override fun onFailure(value: Throwable?) {
                appLog("Falha na tentativa de conexão com iot.eclipse.org")
            }

            // Once we connect..
            override fun onSuccess(v: Void?) {
                appLog("Socket conectado!")
            }
        })
    }

    private fun subscribeToTopic() {
        val topics = arrayOf(Topic("tcc_light_control_infnet", QoS.AT_LEAST_ONCE))
        connection.subscribe(topics, object : Callback<ByteArray> {
            override fun onSuccess(qoses: ByteArray?) {
                // The result of the subcribe request.
                appLog("Inscrito no tópico tcc_light_control_infnet!")
                appLog("Procurando Raspberry Pi 3...")
                sendCommand("command_is_raspberry_alive")
            }

            override fun onFailure(value: Throwable?) {
                appLog("Falha na inscrição do tópico...")
            }
        })
    }

    private fun sendCommand(command: String) {
        sendCommand(command, null, null)
    }

    private fun sendCommand(command: String, successMsg: String?, failMsg: String?) {
        // Send a message to a topic
        connection.publish("tcc_light_control_infnet", command.toByteArray(), QoS.AT_LEAST_ONCE, false, object : Callback<Void> {
            override fun onSuccess(v: Void?) {
                if (successMsg != null) {
                    appLog(successMsg)
                }
            }

            override fun onFailure(value: Throwable?) {
                if (failMsg != null) {
                    appLog(failMsg)
                }
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

    override fun onPowerButtonClick(controlledLight: ControlledLight, turnOn: Boolean) =
            if (turnOn) {
                sendCommand("command_turn_light_on_id_" + controlledLight.id,
                        "Requisitando ação para ligar " + controlledLight.area + "...",
                        "Falha ao tentar desligar luz de " + controlledLight.area)
            } else {
                sendCommand("command_turn_light_off_id_" + controlledLight.id,
                        "Requisitando ação para desligar " + controlledLight.area + "...",
                        "Falha ao tentar desligar luz de " + controlledLight.area)
            }
}

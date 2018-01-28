package at.obyoxar.habrewles.events

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttEventSource(broker: String, clientId: String,val subscriptions: List<String>) : EventSource(subscriptions) {

    private val mqttClient: MqttClient = MqttClient(broker, clientId, MemoryPersistence())

    override fun startListening() {
        val connOpts = MqttConnectOptions()
        connOpts.isCleanSession = true
        mqttClient.connect(connOpts)

        mqttClient.setCallback(object: MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                invoke(topic?:"", Event())
            }

            override fun connectionLost(cause: Throwable?) {
                throw RuntimeException("MQTT-Broker lost connection")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                throw IllegalStateException("This should theoretically never be called")
            }

        })
        mqttClient.subscribe(subscriptions.toTypedArray())
    }

    override fun stopListening() {
        mqttClient.disconnect()
    }
}
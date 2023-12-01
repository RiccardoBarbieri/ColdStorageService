package codedqactor

import it.unibo.kactor.ActorBasic
import org.eclipse.paho.client.mqttv3.MqttMessage
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.utils.CommUtils

class SonarReceiver(name: String) : ActorBasic(name) {

//    init {
//        connectToMqttBroker("tcp://mqtt.eclipseprojects.io")
//        subscribe("unibo/sonar/events")
//    }

    override suspend fun actorBody(msg: IApplMessage) {
        if (msg.msgId() != "distance") return
        if (msg.msgSender() == name) return
        elabData(msg)
    }

//    override fun messageArrived(topic: String, msg: MqttMessage) {
//        super.messageArrived(topic, msg)
//    }

    suspend fun elabData(msg: IApplMessage) {
        val data = msg.msgContent()
        val newMsg = CommUtils.buildEvent(name, "distance", "distance($data)")
        emit(newMsg)
        println("$tt $name |  emits = $newMsg ____________")
    }


}
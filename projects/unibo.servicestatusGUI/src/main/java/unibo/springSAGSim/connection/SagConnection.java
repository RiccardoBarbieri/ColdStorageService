package unibo.springSAGSim.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;

@Component
public class SagConnection extends ConnectionUtils {

    private static final String className = "SagConnection";
    @Value("${actor.ctx}")
    String actorCtx;
    @Value("${actor.name}")
    String actorName;
    @Value("${actor.ctx.port}")
    String actorCtxPort;
    @Value("${actor.ctx.host}")
    String actorCtxHost;


    public CoapConnectionHighTimeout connectLocalActorUsingCoap() {
        return connectActorUsingCoap(actorCtxHost, actorCtxPort, actorCtx, actorName);
    }

    public String sendInitColdRoom(Interaction conn) {
        String functionName = "sendInitColdRoom";
        try {
            String msg = "" + CommUtils.buildRequest("ServiceStatusGUI",
                    "initcoldroom", "initcoldroom(arg)", actorName);
            return sendRequest(conn, msg, functionName);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return null;
    }

    public String sendInitRequestsRejected(Interaction conn) {
        String functionName = "sendInitRequestsRejected";
        try {
            String msg = "" + CommUtils.buildRequest("ServiceStatusGUI",
                    "initreqrejected", "initreqrejected(arg)", actorName);
            return sendRequest(conn, msg, functionName);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return null;
    }

    public String sendInitStatoTT(Interaction conn) {
        String functionName = "sendInitStatoTT";
        try {
            String msg = "" + CommUtils.buildRequest("ServiceStatusGUI",
                    "initstatett", "initstatett(arg)", actorName);
            return sendRequest(conn, msg, functionName);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return null;
    }


    private String sendRequest(Interaction conn, String msg, String functionName) {
        String answer = "";
        try {
            CommUtils.outblue(className + " " + functionName + " | msg:" + msg + ", conn: " + conn);
            answer = conn.request(msg);
            CommUtils.outmagenta(className + " " + functionName + " | answer: " + answer);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return answer;
    }
}
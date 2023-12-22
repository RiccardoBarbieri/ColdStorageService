package unibo.springSAGSim.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import unibo.basicomm23.coap.CoapConnection;
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

    public String sendStorageRequest(Interaction conn, float fw) {
        String functionName = "sendStorageRequest";
        try {
            String msg = "" + CommUtils.buildRequest("serviceaccessgui",
                    "storerequest", "storerequest(" + fw + ")", actorName);
            return sendRequest(conn, msg, functionName);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return null;
    }

    public String sendChargeStatusRequest(Interaction conn) {
        String functionName = "sendChargeStatusRequest";
        try {
            String msg = "" + CommUtils.buildRequest("serviceaccessgui",
                    "chargestatus", "chargestatus(arg)", actorName);
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

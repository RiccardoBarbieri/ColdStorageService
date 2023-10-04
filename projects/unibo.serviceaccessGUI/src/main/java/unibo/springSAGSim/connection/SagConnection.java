package unibo.springSAGSim.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;

@Component
public class SagConnection extends ConnectionUtils{

    private static final String className        = "SagConnection";
    @Value("${actor.ctx}")
    String actorCtx;
    @Value("${actor.name}")
    String actorName;
    @Value("${actor.ctx.port}")
    String actorCtxPort;
    @Value("${actor.ctx.host}")
    String actorCtxHost;


    public CoapConnection connectLocalActorUsingCoap(){
        return connectActorUsingCoap(actorCtxHost, actorCtxPort, actorCtx, actorName);
    }

    public String sendStorageRequest(Interaction conn, int fw){
        String answer = "";
        try {
            String msg = ""+ CommUtils.buildRequest("ServiceAccessGUI",
                    "storerequest", "storerequest("+fw+")", "ColdStorageService");
            CommUtils.outblue(className + " sendStorageRequest | msg:" + msg + ", conn: " + conn);
            answer = conn.request( msg );
            CommUtils.outmagenta(className + " sendStorageRequest | answer: " + answer);
        } catch (Exception e) {
            CommUtils.outred(className + " sendStorageRequest | ERROR: " + e.getMessage());
        }
        return answer;
    }

    public String enterTicketRequest(Interaction conn, String ticketCode) {
        String answer = "";
        try {
            String msg = ""+ CommUtils.buildRequest("ServiceAccessGUI",
                    "ticketrequest", "ticketrequest("+ticketCode+")", "ColdStorageService");
            CommUtils.outblue(className + " enterTicketRequest | msg:" + msg + ", conn: " + conn);
            answer = conn.request( msg );
            CommUtils.outmagenta(className + " enterTicketRequest | answer: " + answer);
        } catch (Exception e) {
            CommUtils.outred(className + " enterTicketRequest | ERROR: " + e.getMessage());
        }
        return answer;
    }
}

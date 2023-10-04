package unibo.springSAGSim.connection;

import org.springframework.beans.factory.annotation.Value;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.utils.CommSystemConfig;
import unibo.basicomm23.utils.CommUtils;

public class ConnectionUtils {
    private static final String className        = "ConnectionUtils";
    private static Interaction coapconn;
    public static CoapConnection connectActorUsingCoap(String acotCtxAddr, String actorCtxPort, String actorCtx, String actorName){
        try {
            CommSystemConfig.tracing = true;
            String path   = actorCtx+"/"+actorName;

            coapconn = new CoapConnection(acotCtxAddr+":"+ actorCtxPort, path );
            CommUtils.outyellow(className + " connectActorUsingCoap | conn:" + coapconn);
        }catch(Exception e){
            CommUtils.outred(className + " connectActorUsingCoap | ERROR: " + e.getMessage());
        }
        return (CoapConnection) coapconn;
    }

}

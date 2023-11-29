package unibo.connection;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;

public class Connection extends ConnectionUtils {

    private static final String className = "Connection";
    String actorCtx = "ctx_coldstorageservice";
    String actorName = "coldstorageservice";
    String actorCtxPort = "8021";
    String actorCtxHost = "localhost";


    public CoapConnectionHighTimeout connectLocalActorUsingCoap() {
        return connectActorUsingCoap(actorCtxHost, actorCtxPort, actorCtx, actorName);
    }

    public String sendInitColdRoom(Interaction conn) {
        String functionName = "sendInitColdRoom";
        try {
            String msg = "" + CommUtils.buildRequest("ServiceAccessGUI", "initcoldroom", "initcoldroom(arg)", actorName);
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

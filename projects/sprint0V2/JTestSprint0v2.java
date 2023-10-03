import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import unibo.basicomm23.utils.CommUtils;

public class JTestSprint0v2 {

	private final static String className = "JUnitTest";

	@Value("${actor.ctx}")
	String actorCtx;
	@Value("${actor.name}")
	String actorName;
	@Value("${actor.ctx.port}")
	String actorCtxPort;
	@Value("${actor.ctx.host}")
	String actorCtxHost;

	private Interaction coapconn;

	public String sendStorageRequest(Interaction conn, float fw){
		String answer = "";
		try {
			String msg = ""+ CommUtils.buildRequest(className,
					"storerequest", "storerequest("+fw+")", "ColdStorageService");
			CommUtils.outblue(className + " sendStorageRequest | msg:" + msg + ", conn: " + conn);
			answer = conn.request( msg );
			CommUtils.outmagenta(className + " sendStorageRequest | answer: " + answer);
		} catch (Exception e) {
			CommUtils.outred(className + " sendStorageRequest | ERROR: " + e.getMessage());
		}
		return answer;
	}

	@Test
	public void test() {
		if (conn == null) {
			fail(className + " sendStorageRequest | ERROR: connection null");
		}
		String answer = sagConnection.sendStorageRequest(conn, fwrequest.getFw());
		if (answer == null) {
			fail(className + " sendStorageRequest | ERROR: response null");
		}
	}
	
	@Before
	public void init() {
		try {
			String path   = actorCtx+"/"+actorName;
			coapconn = new CoapConnection(acotCtxAddr+":"+ actorCtxPort, path );
			CommUtils.outyellow(className + " connectActorUsingCoap | conn:" + coapconn);
		}catch(Exception e){
			fail(className + " getCoapConnection | ERROR: " + e.getMessage());
		}
	}

}

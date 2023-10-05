import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.*;

public class JTestSprint0v2 {

	private final static String className = "JUnitTest";
	
	private final static String actorName = "coldstorageservice";
	private final static String actorCtx = "ctx_coldstorageservice";
	private final static String actorCtxPort = "8021";
	private final static String actorCtxHost = "localhost";

	private final static Float MAXW = 50;

	private Interaction coapconn;

	private String sendStorageRequest(String fw){
		return sendMessage("storerequest", fw.toString());
	}
	
	private String sendTicketRequest(){
		return sendMessage("deposit", "");
	}
	
	private String sendChargeStatusRequest(){
		return sendMessage("chargestatus", "");
	}
	
	private String sendMessage(String messageName, String argument) {
		String answer = "";
		try {
			String msg = ""+ CommUtils.buildRequest(className,
					messageName, messageName + "("+argument+")", "ColdStorageService");
			CommUtils.outyellow(className + " " + messageName + "Request | msg:" + msg + ", coapconn: " + coapconn);
			answer = coapconn.request( msg );
		} catch (Exception e) {
			CommUtils.outred(className + " " + messageName + "Request | ERROR: " + e.getMessage());
		}
		if (answer == null) {
			fail(className + " " + messageName + "Request | ERROR: response null");
		}
		return answer;
	}
	
	private void implementProtocol(Float fw) {
		String answer = sendStorageRequest(fw.toString());
		if (fw <= MAXW) {
			assertTrue(answer.contains("loadaccepted"));

			answer = sendTicketRequest();
			assertTrue(answer.contains("ticketaccepted"));
			
			answer = sendChargeStatusRequest();
			assertTrue(answer.contains("chargetaken"));	
		}
		else {
			assertTrue(answer.contains("loadrejected"));
		}
	}

	@Test
	public void testRichiestaOK() {
		implementProtocol(Float.valueOf(20));
	}
	
	@Test
	public void testRichiestaKO() {
		implementProtocol(Float.valueOf(60));
	}
	
	@Before
	public void init() {
		try {
			String path   = actorCtx+"/"+actorName;
			coapconn = new CoapConnection(actorCtxHost+":"+ actorCtxPort, path );
			CommUtils.outyellow(className + " getCoapConnection | conn:" + coapconn);
		}catch(Exception e){
			fail(className + " getCoapConnection | ERROR: " + e.getMessage());
		}
		if (coapconn == null) {
			fail(className + " getCoapConnection | ERROR: connection null");
		}
	}

}

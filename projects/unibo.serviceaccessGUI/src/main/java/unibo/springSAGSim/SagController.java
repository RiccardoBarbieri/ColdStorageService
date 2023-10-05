package unibo.springSAGSim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.springSAGSim.connection.CoapObserver;
import unibo.springSAGSim.connection.SagConnection;
import unibo.springSAGSim.model.FWRequest;
import unibo.springSAGSim.model.TicketRequest;

@Controller
public class SagController {
    
    public static final String className = "SagController";
    private SagConnection sagConnection;
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("arg", appName);
        return "main";
    }

    @Autowired
    public SagController(SagConnection sagConnection) {
        this.sagConnection = sagConnection;
        CoapConnection conn = sagConnection.connectLocalActorUsingCoap();
        conn.observeResource(new CoapObserver());
    }

    @PostMapping(value = "/sendStorageRequest", consumes = "application/json")
    public ResponseEntity sendStorageRequest(@RequestBody FWRequest fwrequest){
        if (fwrequest == null || fwrequest.getFw() == null || fwrequest.getFw() <= 0) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity(className + " sendStorageRequest | ERROR: input error", headers, HttpStatus.BAD_REQUEST);
        }

        Interaction conn = sagConnection.connectLocalActorUsingCoap();
        if (conn == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity(className + " sendStorageRequest | ERROR: connection null", headers, HttpStatus.NOT_FOUND);
        }

        String answer = sagConnection.sendStorageRequest(conn, fwrequest.getFw());
        if (answer == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity(className + " sendStorageRequest | ERROR: response null", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity(className + " sendStorageRequest | answer: " + answer, headers, HttpStatus.OK);
    }


    @PostMapping(value = "/enterTicketRequest", consumes = "application/json")
    public ResponseEntity enterTicketRequest(@RequestBody TicketRequest ticketrequest){
        if (ticketrequest == null || ticketrequest.getTicketCode() == null || ticketrequest.getTicketCode().equals("")) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity(className + " sendTicketNumber | ERROR: input error", headers, HttpStatus.BAD_REQUEST);
        }

        Interaction conn = sagConnection.connectLocalActorUsingCoap();
        if (conn == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity(className + " sendTicketNumber | ERROR: connection null", headers, HttpStatus.NOT_FOUND);
        }

        String answer = sagConnection.enterTicketRequest(conn, ticketrequest.getTicketCode());
        if (answer == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity(className + " sendTicketNumber | ERROR: response null", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity(className + " sendTicketNumber | answer: " + answer, headers, HttpStatus.OK);
    }
}
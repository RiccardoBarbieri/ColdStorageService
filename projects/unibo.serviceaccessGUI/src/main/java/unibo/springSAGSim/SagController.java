package unibo.springSAGSim;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.springSAGSim.connection.CoapObserver;
import unibo.springSAGSim.connection.SagConnection;
import unibo.springSAGSim.model.FWRequest;
import unibo.springSAGSim.model.TicketRequest;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
public class SagController {

    public static final String className = "SagController";

    private SagConnection sagConnection;
    private CoapConnection observerConn;
    private Interaction requestConn;

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    public SagController(SagConnection sagConnection) {
        this.sagConnection = sagConnection;
        this.observerConn = sagConnection.connectLocalActorUsingCoap();
        observerConn.observeResource(new CoapObserver());
        this.requestConn = sagConnection.connectLocalActorUsingCoap();
    }

    @GetMapping("/")
    public String homePage(Model model) {
        String temp = "ERROR";
        String actual = "ERROR";

        model.addAttribute("arg", appName);

        if (this.requestConn == null) {
            model.addAttribute("tempCurrentColdRoom", temp);
            model.addAttribute("actualCurrentColdRoom", actual);
        }

        String answer = sagConnection.sendInitColdRoom(this.requestConn);
        if (answer == null) {
            model.addAttribute("tempCurrentColdRoom", temp);
            model.addAttribute("actualCurrentColdRoom", actual);
        }
        else {
            String both = answer.substring(answer.indexOf("coldroom(") + 9, answer.indexOf(")"));
            temp = both.split(",")[0];
            actual = both.split(",")[1];
        }
        model.addAttribute("tempCurrentColdRoom", temp + " KG");
        model.addAttribute("actualCurrentColdRoom", actual + " KG");

        return "main";
    }

    @PostMapping(value = "/sendStorageRequest", consumes = "application/json")
    public ResponseEntity<String> sendStorageRequest(@RequestBody FWRequest fwrequest) {
        if (fwrequest == null || fwrequest.getFw() == null || fwrequest.getFw() < 0) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " sendStorageRequest | ERROR: input error", headers, HttpStatus.BAD_REQUEST);
        }

        if (this.requestConn == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " sendStorageRequest | ERROR: connection null", headers, HttpStatus.NOT_FOUND);
        }

        String answer = sagConnection.sendStorageRequest(this.requestConn, fwrequest.getFw());
        if (answer == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " sendStorageRequest | ERROR: response null", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(className + " sendStorageRequest | answer: " + answer, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/sendChargeStatusRequest")
    public ResponseEntity<String> sendChargeStatusRequest() {

        String answer = sagConnection.sendChargeStatusRequest(this.requestConn);
        if (answer == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " sendChargeStatusRequest | ERROR: response null", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(className + " sendChargeStatusRequest | answer: " + answer, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/enterTicketRequest", consumes = "application/json")
    public ResponseEntity<String> enterTicketRequest(@RequestBody TicketRequest ticketrequest){
        if (ticketrequest == null || ticketrequest.getTicketCode() == null || ticketrequest.getTicketCode().isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " enterTicketRequest | ERROR: input error", headers, HttpStatus.BAD_REQUEST);
        }

        Interaction conn = sagConnection.connectLocalActorUsingCoap();
        if (conn == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " enterTicketRequest | ERROR: connection null", headers, HttpStatus.NOT_FOUND);
        }

        String answer = sagConnection.enterTicketRequest(conn, ticketrequest.getTicketCode());
        if (answer == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " enterTicketRequest | ERROR: response null", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(className + " enterTicketRequest | answer: " + answer, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/generatePdf", consumes = "application/json", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf(@RequestBody TicketRequest ticketrequest) {
        Document document = new Document(PageSize.A7);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
        } catch (DocumentException e) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);

            Paragraph paragraph = new Paragraph("Il tuo ticket:", font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            paragraph = new Paragraph(ticketrequest.getTicketCode(), font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            document.close();
        } catch (DocumentException e) {
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ticket.pdf");
        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }

}
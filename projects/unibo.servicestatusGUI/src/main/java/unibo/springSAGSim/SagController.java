package unibo.springSAGSim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.springSAGSim.connection.SagConnection;
import unibo.springSAGSim.connection.websocket.CoapObserver;

@Controller
public class SagController {

    public static final String className = "SagController";
    @Value("${spring.application.name}")
    String appName;
    private SagConnection sagConnection;
    private CoapConnection observerConn;
    private Interaction requestConn;

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
        String reqRejected = "ERROR";
        String stateTT = "ERROR";
        String posTT = "ERROR";

        model.addAttribute("arg", appName);

        if (this.requestConn == null) {
            model.addAttribute("tempCurrentColdRoom", temp);
            model.addAttribute("actualCurrentColdRoom", actual);
            model.addAttribute("requestsRejected", reqRejected);
            model.addAttribute("stateTT", stateTT);
            model.addAttribute("posTT", posTT);
            return "main";
        }

        String answer = sagConnection.sendInitColdRoom(this.requestConn);
        if (answer != null) {
            String both = answer.substring(answer.indexOf("coldroom(") + 9, answer.indexOf(")"));
            actual = both.split(",")[0] + " KG";
            temp = both.split(",")[1] + " KG";
        }
        model.addAttribute("tempCurrentColdRoom", temp);
        model.addAttribute("actualCurrentColdRoom", actual);
        

        answer = sagConnection.sendInitRequestsRejected(this.requestConn);
        if (answer != null) {
            reqRejected = answer.split("reqRejected(")[1].split(")")[0];
        }
        model.addAttribute("requestsRejected", reqRejected);


        answer = sagConnection.sendInitStatoTT(this.requestConn);
        if (answer != null) {
           String both = answer.split("stateTT(")[1].split(")")[0];
           stateTT = both.split(",")[0];
           posTT = both.split(",")[1];
        }
        model.addAttribute("stateTT", stateTT);
        model.addAttribute("posTT", posTT);

        return "main";
    }
}
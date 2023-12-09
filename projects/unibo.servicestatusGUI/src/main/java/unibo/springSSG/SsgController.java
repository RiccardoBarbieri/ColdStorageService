package unibo.springSSG;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.springSSG.connection.SsgConnection;
import unibo.springSSG.connection.websocket.CoapObserver;

@Controller
public class SsgController {

    public static final String className = "SagController";
    @Value("${spring.application.name}")
    String appName;
    private SsgConnection ssgConnection;
    private CoapConnection observerConn;
    private Interaction requestConn;

    @Autowired
    public SsgController(SsgConnection ssgConnection) {
        this.ssgConnection = ssgConnection;
        this.observerConn = ssgConnection.connectLocalActorUsingCoap();
        observerConn.observeResource(new CoapObserver());
        this.requestConn = ssgConnection.connectLocalActorUsingCoap();
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

        String answer = ssgConnection.sendInitColdRoom(this.requestConn);
        if (answer != null) {
            String both = answer.substring(answer.indexOf("coldroom(") + 9, answer.indexOf(")"));
            actual = both.split(",")[0] + " KG";
            temp = both.split(",")[1] + " KG";
        }
        model.addAttribute("tempCurrentColdRoom", temp);
        model.addAttribute("actualCurrentColdRoom", actual);
        

        answer = ssgConnection.sendInitRequestsRejected(this.requestConn);
        if (answer != null) {
            reqRejected = answer.split("reqrejected\\(")[1].split("\\)")[0];
        }
        model.addAttribute("requestsRejected", reqRejected);


        answer = ssgConnection.sendInitStatoTT(this.requestConn);
        if (answer != null) {
           String both = answer.split("statett\\(")[1].split("\\)")[0];
           stateTT = both.split(",")[0];
           posTT = both.split(",")[1];
        }
        model.addAttribute("stateTT", stateTT);
        model.addAttribute("posTT", posTT);

        return "main";
    }
}
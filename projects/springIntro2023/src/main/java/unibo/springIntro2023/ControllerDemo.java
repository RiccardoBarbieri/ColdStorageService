package unibo.springIntro2023;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import unibo.springIntro2023.interfaces.MapService;
import unibo.springIntro2023.model.MapConfiguration;

@Controller
public class ControllerDemo {

    public static final Logger logger = LoggerFactory.getLogger(ControllerDemo.class);
    @Value("${spring.application.name}")
    String appName;

    private final MapService mapService;

    @GetMapping("/")
    public String homePage() {
        return "configurator";
    }

    @Autowired
    public ControllerDemo(@Qualifier("gridServiceRoomMap") MapService mapService) {
        this.mapService = mapService;
    }

    @PostMapping(value = "/savegrid", consumes = "application/json")
    public ResponseEntity saveGrid(@RequestBody @Validated MapConfiguration mapConfiguration, BindingResult result) {
        if (result.hasErrors()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity("ControllerDemo ERROR " + result.getAllErrors(), headers, HttpStatus.BAD_REQUEST);
        }

        mapService.dumpMap(mapConfiguration);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity("ControllerDemo " + appName, headers, HttpStatus.OK);

    }

    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity("ControllerDemo ERROR " + ex.getMessage(), headers, HttpStatus.CREATED);
    }

}

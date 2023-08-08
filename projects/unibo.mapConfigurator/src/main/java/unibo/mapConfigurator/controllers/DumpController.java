package unibo.mapConfigurator.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import unibo.mapConfigurator.interfaces.MapService;
import unibo.mapConfigurator.model.MapConfiguration;
import org.springframework.stereotype.Controller;

@Controller
public class DumpController {

    private final MapService mapService;
    @Value("${spring.application.name}")
    String appName;

    @Autowired
    public DumpController(@Qualifier("gridServiceRoomMap") MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/")
    public String homePage() {
        return "configurator";
    }

    @PostMapping(value = "/savegrid", consumes = "application/json")
    public ResponseEntity<String> saveGrid(@RequestBody @Validated MapConfiguration mapConfiguration, BindingResult result) {
        if (result.hasErrors()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>("ControllerDemo ERROR " + result.getAllErrors(), headers, HttpStatus.BAD_REQUEST);
        }

        LoggerFactory.getLogger(DumpController.class).info("Compiling map \"" + mapConfiguration.getName() + "\"");
        if (!mapService.compileMap(mapConfiguration)) {
            LoggerFactory.getLogger(DumpController.class).error("Invalid map representation \"" + mapConfiguration.getCompact() + "\"");
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>("ControllerDemo ERROR " + "Invalid character in map representation", headers, HttpStatus.BAD_REQUEST);
        }

        LoggerFactory.getLogger(DumpController.class).info("Dumping map \"" + mapConfiguration.getName() + "\"");
        if (!mapService.dumpMap(mapConfiguration)) {
            LoggerFactory.getLogger(DumpController.class).error("IOException dumping map \"" + mapConfiguration.getName() + "\"");
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>("ControllerDemo ERROR " + "Error while saving map", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>("ASDASDASDControllerDemo " + appName, headers, HttpStatus.OK);

    }

    @ExceptionHandler
    public ResponseEntity<String> handle(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>("ControllerDemo ERROR " + ex.getMessage(), headers, HttpStatus.CREATED);
    }

}

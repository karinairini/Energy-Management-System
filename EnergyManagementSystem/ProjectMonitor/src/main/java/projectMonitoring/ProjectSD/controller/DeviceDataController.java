package projectMonitoring.ProjectSD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projectMonitoring.ProjectSD.service.DeviceDataService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/device-data")
public class DeviceDataController {
    private final DeviceDataService deviceDataService;

    @Autowired
    public DeviceDataController(DeviceDataService deviceDataService) {
        this.deviceDataService = deviceDataService;
    }

    @DeleteMapping("/deleteAll/{deviceId}")
    public ResponseEntity<String> deleteDeviceDataByDeviceId(@PathVariable UUID deviceId) {
        deviceDataService.deleteDeviceDataByDeviceId(deviceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{deviceId}/values/day")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<Double>> getDeviceDataValuesForDay(@PathVariable("deviceId") UUID deviceId,
                                                                  @RequestParam("date") String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        List<Double> values = deviceDataService.getDeviceDataValuesForDay(deviceId, date);

        return new ResponseEntity<>(values, HttpStatus.OK);
    }
}

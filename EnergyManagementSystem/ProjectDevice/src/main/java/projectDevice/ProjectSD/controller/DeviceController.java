package projectDevice.ProjectSD.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projectDevice.ProjectSD.dto.device.DeviceRequestDTO;
import projectDevice.ProjectSD.dto.device.DeviceResponseDTO;
import projectDevice.ProjectSD.service.DeviceService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/device")
public class DeviceController {
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<DeviceResponseDTO>> getDevicesByUserId(@PathVariable("userId") UUID userId) {
        List<DeviceResponseDTO> deviceResponseDTOList = deviceService.getDevicesByUserId(userId);
        return new ResponseEntity<>(deviceResponseDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable("id") UUID deviceId) {
        DeviceResponseDTO deviceResponseDTO = deviceService.getDeviceById(deviceId);
        return new ResponseEntity<>(deviceResponseDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceResponseDTO> saveDevice(@PathVariable("userId") UUID userId,
                                                        @Valid @RequestBody DeviceRequestDTO deviceRequestDTO) {
        DeviceResponseDTO deviceResponseDTO = deviceService.saveDevice(userId, deviceRequestDTO);
        return new ResponseEntity<>(deviceResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceResponseDTO> updateDevice(@PathVariable("id") UUID deviceId,
                                                          @Valid @RequestBody DeviceRequestDTO deviceRequestDTO) {
        DeviceResponseDTO deviceResponseDTO = deviceService.updateDevice(deviceId, deviceRequestDTO);
        return new ResponseEntity<>(deviceResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") UUID deviceId) {
        deviceService.deleteDevice(deviceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

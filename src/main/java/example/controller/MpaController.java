package example.controller;

import example.dto.response.MpaResponse;
import example.service.MpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;


    @GetMapping("/{id}")
    public MpaResponse findById(@PathVariable Long id) {
        log.info("HTTP GET /mpa/{id} started: id={}", id);
        MpaResponse response = mpaService.findById(id);
        log.info("HTTP GET /mpa/{id} finished: id={}", id);
        return response;
    }

    @GetMapping
    public List<MpaResponse> findAll() {
        log.info("HTTP GET /mpa started");
        List<MpaResponse> responses = mpaService.findAll();
        log.info("HTTP GET /mpa finished count={}", responses.size());
        return responses;
    }
}

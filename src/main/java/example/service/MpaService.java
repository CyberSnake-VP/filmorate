package example.service;

import example.dto.response.MpaResponse;

import java.util.List;

public interface MpaService {
    List<MpaResponse> findAll();
    MpaResponse findById(Long id);
}

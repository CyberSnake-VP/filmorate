package example.service;

import example.dto.response.GenreResponse;

import java.util.List;

public interface GenreService {
    List<GenreResponse> findAll();
    GenreResponse findById(Long id);
}

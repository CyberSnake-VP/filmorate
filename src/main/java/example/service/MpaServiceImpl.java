package example.service;

import example.dal.mpa.MpaRepository;
import example.dto.response.MpaResponse;
import example.exception.NotFoundException;
import example.mapper.MpaMapper;
import example.model.MpaRating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaServiceImpl implements MpaService {
    private final MpaRepository mpaRepository;
    private static final String NOT_FOUND_MESSAGE = "MPA rating not found";

    @Override
    public List<MpaResponse> findAll() {
        log.info("Find all MPA ratings started");
        List<MpaRating> ratings = mpaRepository.findAll();
        log.info("Find all MPA ratings finished");
        return ratings.stream().map(MpaMapper::toMpaResponse).toList();
    }

    @Override
    public MpaResponse findById(Long id) {
        log.info("Find mpa rating by id started: id={}", id);
        MpaRating mpa = mpaRepository.getMpaById(id).orElseThrow(() -> {
            log.warn("Find mpa rating by id finished: id={}", id);
            return new NotFoundException(NOT_FOUND_MESSAGE);
        });
        log.info("Find mpa rating by id finished: id={}", id);
        return MpaMapper.toMpaResponse(mpa);
    }
}

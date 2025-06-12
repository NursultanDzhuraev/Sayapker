package pro.sayapker.repository.jdbcClient;

import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.horse.ClientRequest;
import pro.sayapker.dto.horse.HorseResponse;

public interface HorseJDBC {
    PaginationResponse<HorseResponse> findAllHorse(int pageNumber, int pageSize);

    PaginationResponse<HorseResponse> getAllBookForClient(ClientRequest clientRequest, int pageNumber, int pageSize);

    SimpleResponse deleteHorseById(Long id);
}

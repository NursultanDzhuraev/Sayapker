package pro.sayapker.repository.jdbcClient;

import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.horse.HorseResponse;

public interface HorseJDBC {
    PaginationResponse<HorseResponse> findAllHorse(int pageNumber, int pageSize);
}

package pro.sayapker.service;

import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.horse.HorseRequest;
import pro.sayapker.dto.horse.HorseResponse;
import pro.sayapker.dto.horse.HorseResponseApplication;

public interface HorseService {
    SimpleResponse saveHors(HorseRequest horseRequest);

    PaginationResponse<HorseResponseApplication> findAllHorseApplications(int pageNumber, int pageSize);

    PaginationResponse<HorseResponse> findAllHorse(int pageNumber, int pageSize);
}

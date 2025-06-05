package pro.sayapker.service;

import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.horse.*;

public interface HorseService {
    SimpleResponse saveHors(HorseRequest horseRequest);

    PaginationResponse<HorseResponseApplication> findAllHorseApplications(int pageNumber, int pageSize);

    PaginationResponse<HorseResponse> findAllHorse(int pageNumber, int pageSize);

    SimpleResponse acceptHorseFromApplication(Long horseId);

    SimpleResponse rejectHorseFromApplication(Long horseId, ReasonOfRejectionBookItemRequest reason);

    SimpleResponse updateHorse(Long horseId, HorseRequest horseRequest);

    HorseByIdResponse findByIdHorse(Long horseId);

    SimpleResponse deletedHorseById(Long horseId);
}

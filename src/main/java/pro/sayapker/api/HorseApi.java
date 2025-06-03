package pro.sayapker.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.horse.HorseRequest;
import pro.sayapker.dto.horse.HorseResponse;
import pro.sayapker.dto.horse.HorseResponseApplication;
import pro.sayapker.service.HorseService;

@RestController
@RequestMapping("/api/horse")
@RequiredArgsConstructor
public class HorseApi {
    private final HorseService horseService;
    @GetMapping("/saveHorse")
    public SimpleResponse saveHorse(@RequestBody HorseRequest horseRequest){
        return horseService.saveHors(horseRequest);
    }
    @Secured("ADMIN")
    @Operation(summary = "получить все заявки" ,
            description = "возврощает лист заявок на продажу horse. Доступен только для админа.")
    public PaginationResponse<HorseResponseApplication> findAllHorseApplications(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
    return horseService.findAllHorseApplications(pageNumber,pageSize);
    }
    @Secured("ADMIN")
    @Operation(summary = "получить все заявки" ,
            description = "возврощает лист заявок на продажу horse. Доступен только для админа.")
    public PaginationResponse<HorseResponse> findAllHorse(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return horseService.findAllHorse(pageNumber,pageSize);
    }

}

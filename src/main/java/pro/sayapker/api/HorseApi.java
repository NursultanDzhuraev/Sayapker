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
import pro.sayapker.dto.horse.ReasonOfRejectionBookItemRequest;
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
    @Secured("ADMIN")
    @Operation(
            summary = "Принятие книги из заявки",
            description = "Позволяет администратору принять книгу из раздела заявок.")
    @PatchMapping("/acceptBookItemByIdFromApplication")
    public SimpleResponse acceptHorseFromApplication(@RequestParam Long horseId) {
        return horseService.acceptHorseFromApplication(horseId);
    }
    @Secured("ADMIN")
    @Operation(
            summary = "Отклонение книги из заявки",
            description = "Позволяет администратору отклонить книгу из раздела заявок. ")
    @PostMapping("/rejectBookItemByIdFromApplication")
    public SimpleResponse rejectHorseFromApplication(@RequestParam Long horseId,
                                                               @RequestBody ReasonOfRejectionBookItemRequest reason) {
        return horseService.rejectHorseFromApplication(horseId, reason);
    }


}

package pro.sayapker.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.horse.*;
import pro.sayapker.service.HorseService;

@RestController
@RequestMapping("/api/horse")
@RequiredArgsConstructor
public class HorseApi {
    private final HorseService horseService;
    @Secured({"ADMIN","USER"})
    @GetMapping("/saveHorse")
    public SimpleResponse saveHorse(@RequestBody HorseRequest horseRequest){
        return horseService.saveHors(horseRequest);
    }
    @Secured("ADMIN")
    @Operation(summary = "получить все заявки" ,
            description = "возврощает лист заявок на продажу horse. Доступен только для админа.")
    @GetMapping("/findAllHorseApplications")
    public PaginationResponse<HorseResponseApplication> findAllHorseApplications(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
    return horseService.findAllHorseApplications(pageNumber,pageSize);
    }
    @Operation(summary = "получить все horses " )
    @GetMapping("/findAllHorse")
    public PaginationResponse<HorseResponse> findAllHorse(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return horseService.findAllHorse(pageNumber,pageSize);
    }
    @Secured("ADMIN")
    @Operation(
            summary = "Принятие horse из заявки",
            description = "Позволяет администратору принять horse из раздела заявок.")
    @PostMapping("/acceptHorseFromApplication/{horseId}")
    public SimpleResponse acceptHorseFromApplication(@PathVariable Long horseId) {
        return horseService.acceptHorseFromApplication(horseId);
    }
    @Secured("ADMIN")
    @Operation(
            summary = "Отклонение horse из заявки",
            description = "Позволяет администратору отклонить horse из раздела заявок. ")
    @PostMapping("/rejectHorseFromApplication/{horseId}")
    public SimpleResponse rejectHorseFromApplication(@PathVariable Long horseId,
                                                     @RequestBody ReasonOfRejectionBookItemRequest reason) {
        return horseService.rejectHorseFromApplication(horseId, reason);
    }
    @Secured("USER")
    @Operation(summary = "Редактировать horse", description = "Редактировать horse могут владелец" )
    @PutMapping("/updateHorse/{horseId}")
    public SimpleResponse updateHorse(@PathVariable Long horseId,@RequestBody HorseRequest horseRequest){
        return horseService.updateHorse(horseId,horseRequest);
    }
    @GetMapping("/findByIdHorse/{horseId}")
    public HorseByIdResponse findByIdHorse(@PathVariable Long horseId){
        return horseService.findByIdHorse(horseId);
    }
    @DeleteMapping("/deletedById/{horseId}")
    public SimpleResponse deletedById(@PathVariable Long horseId){
        return horseService.deletedHorseById(horseId);
    }

    @Operation(summary = " Фильтрация и сортировка horse по параметрам",
            description = "получить  всех horse по параметрам")
    @PostMapping("/getAllBookForClient")
    public PaginationResponse<HorseResponse> getAllBookForClient(
            @RequestBody ClientRequest clientRequest,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return horseService.getAllBookForClient(clientRequest,pageNumber,pageSize);
    }
}

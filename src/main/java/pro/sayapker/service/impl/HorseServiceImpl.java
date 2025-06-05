package pro.sayapker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.horse.*;
import pro.sayapker.entity.Horse;
import pro.sayapker.entity.User;
import pro.sayapker.enums.Status;
import pro.sayapker.repository.HorseRepo;
import pro.sayapker.repository.UserRepo;
import pro.sayapker.repository.jdbcClient.HorseJDBC;
import pro.sayapker.service.HorseService;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorseServiceImpl implements HorseService {
private final HorseRepo horseRepo;
private final UserRepo userRepo;
private final HorseJDBC horseJDBC;
    @Override
    public SimpleResponse saveHors(HorseRequest horseRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmailOrElseThrow(email);
        Horse horse = new Horse();
        horse.setName(horseRequest.getName());
        horse.setStatus(Status.PENDING);
        horse.setUser(user);
        horse.setRegistrationDate(LocalDate.now());
        horse.setBreed(horseRequest.getBreed());
        horse.setGender(horseRequest.getGender());
        horse.setAncestors(horseRequest.getAncestors());
        horse.setHomeland(horseRequest.getHomeland());
        horse.setInformation(horseRequest.getInformation());
        horse.setBirthDate(horseRequest.getBirthDate());
        horse.setImages(horseRequest.getImages());
        horse.setBirthDate(horseRequest.getBirthDate());
        horseRepo.save(horse);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("Успешно сохранено ")
                .build();
    }

    @Override
    public PaginationResponse<HorseResponseApplication> findAllHorseApplications(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        Page<Horse> horsePage = horseRepo.findAllHorse(pageable);
        var response = new PaginationResponse<HorseResponseApplication>();
        response.setPageNumber(pageNumber+1);
        response.setPageSize(pageSize);
        response.setTotalElements(horsePage.getTotalElements());
        response.setTotalPages(horsePage.getTotalPages());
        response.setContent(getList(horsePage.getContent()));
        return response;
    }

    @Override
    public PaginationResponse<HorseResponse> findAllHorse(int pageNumber, int pageSize) {
      return horseJDBC.findAllHorse(pageNumber,pageSize);
    }

    @Override
    public SimpleResponse acceptHorseFromApplication(Long horseId) {
        Horse horse = horseRepo.findByIdHorse(horseId);
        horse.setStatus(Status.ACCEPTED);
        horseRepo.save(horse);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.ACCEPTED)
                .message("Успешно принятая horse")
                .build();
    }

    @Override
    public SimpleResponse rejectHorseFromApplication(Long horseId, ReasonOfRejectionBookItemRequest reason) {
        Horse horse = horseRepo.findByIdHorse(horseId);
        horse.setStatus(Status.REJECTED);
        horse.setReasonOfRejection(reason.getReason());
        horseRepo.save(horse);
      return SimpleResponse.builder()
              .httpStatus(HttpStatus.ACCEPTED)
              .message("Успешно отклонена horse")
              .build();
    }

    @Override
    public SimpleResponse updateHorse(Long horseId, HorseRequest horseRequest) {
        Horse horse = horseRepo.findHorseById(horseId);
        horse.setName(horseRequest.getName());
        horse.setStatus(Status.PENDING);
        horse.setBreed(horseRequest.getBreed());
        horse.setGender(horseRequest.getGender());
        horse.setAncestors(horseRequest.getAncestors());
        horse.setHomeland(horseRequest.getHomeland());
        horse.setInformation(horseRequest.getInformation());
        horse.setBirthDate(horseRequest.getBirthDate());
        horse.setImages(horseRequest.getImages());
        horseRepo.save(horse);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно редактировано horse")
                .build();
    }

    @Override
    public HorseByIdResponse findByIdHorse(Long horseId) {
        Horse horse = horseRepo.findHorseById(horseId);
        User user = horse.getUser();
        return HorseByIdResponse.builder()
                .horseName(horse.getName())
                .horseId(horse.getId())
                .images(horse.getImages())
                .phoneNumber(user.getPhoneNumber())
                .ownerName(user.getFirstName() + " " + user.getLastName())
                .dataOfBirthday(horse.getBirthDate())
                .horseId(horse.getId())
                .ownerImage(user.getImageUrl())
                .build();
    }

    @Override
    public SimpleResponse deletedHorseById(Long horseId) {
        Horse horse = horseRepo.findHorseById(horseId);
        horseRepo.delete(horse);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно удалено")
                .build();
    }

    private HorseResponseApplication getHorseResponseApplication(Horse horse) {
        User user = horse.getUser();
        User user1 = userRepo.findByIdlOrElseThrow(user.getId());
        List<String> images = horse.getImages();
      return   HorseResponseApplication.builder()
                .horseId(horse.getId())
                .horseName(horse.getName())
                .ownerName(user1.getUsername())
                .ownerImage(user1.getImageUrl())
                .dataOfBirthday(horse.getBirthDate())
                .image(images.get(0))
                .build();
    }
    private List<HorseResponseApplication> getList(List<Horse> horseList) {
       return horseList.stream().map(this::getHorseResponseApplication).toList();
    }
}

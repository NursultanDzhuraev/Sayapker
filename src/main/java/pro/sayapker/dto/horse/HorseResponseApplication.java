package pro.sayapker.dto.horse;

import lombok.Builder;

import java.util.Date;

@Builder
public record HorseResponseApplication(
        Long horseId,
        String horseName,
        String ownerImage,
        String ownerName,
        Date dataOfBirthday,
        String image
) {
}

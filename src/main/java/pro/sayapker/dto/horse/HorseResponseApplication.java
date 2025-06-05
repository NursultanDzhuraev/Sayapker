package pro.sayapker.dto.horse;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Builder
public record HorseResponseApplication(
        Long horseId,
        String horseName,
        String ownerImage,
        String ownerName,
        BigDecimal price,
        LocalDate dataOfBirthday,
        String image
) {
}

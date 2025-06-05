package pro.sayapker.dto.horse;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Builder
public record HorseByIdResponse(
        Long horseId,
        String horseName,
        String ownerImage,
        String ownerName,
        BigDecimal price,
        LocalDate dataOfBirthday,
        List<String> images,
        String phoneNumber
        ) {
}

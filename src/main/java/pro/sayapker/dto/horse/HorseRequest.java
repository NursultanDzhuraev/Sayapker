package pro.sayapker.dto.horse;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import pro.sayapker.enums.Gender;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Getter
@Setter
public class HorseRequest {
    @NotBlank(message = "поле name не должен быть пустым")
    @Size(min = 1, max = 200, message = "длина name должна быть от 1 до 200 символов")
    private String name;

    @NotNull(message = "поле birthDate не должен быть пустым")
    @PastOrPresent(message = "дата рождения не может быть в будущем")
    private Date birthDate;

    @NotNull(message = "поле gender не должен быть пустым")
    @Pattern(regexp = "MALE|FEMALE", message = "поле gender должно быть MALE или FEMALE")
    private Gender gender;

    @Size(min = 2, max = 200, message = "длина breed должна быть от 2 до 200 символов")
    private String breed;

    @Size(min = 2, max = 200, message = "длина homeland должна быть от 2 до 200 символов")
    private String homeland;

    @NotNull(message = "список изображений не должен быть пустым")
    @Size(min = 1, max = 10, message = "список изображений должен содержать от 1 до 10 элементов")
    private List<String> images;

    @Size(max = 500, message = "информация должна содержать не более 500 символов")
    private String information;

    private Map<String, String> ancestors;
}

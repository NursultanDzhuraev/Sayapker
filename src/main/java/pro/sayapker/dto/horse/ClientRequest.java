package pro.sayapker.dto.horse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientRequest {
    private String name;
    private LocalDate birthDate;
    private String homeland;
    private int startPrice;
    private int endPrice;
}

package pro.sayapker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pro.sayapker.enums.Gender;
import pro.sayapker.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "horses")
public class Horse {
    @Id
    @GeneratedValue(generator = "horse_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "horse_gen", sequenceName = "horse_seq", allocationSize = 1, initialValue = 100)
    private Long id;
    private String name;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String breed;
    private String homeland;
    @ElementCollection
    private List<String> images;
    private BigDecimal price;
    private String information;
    @ElementCollection
    private Map<String,String> ancestors;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String reasonOfRejection;
    private LocalDate registrationDate;

    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "horse")
    private List<Like> likes;

}

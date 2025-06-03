package pro.sayapker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pro.sayapker.enums.Gender;
import pro.sayapker.enums.Status;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "horses")
public class Horse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date birthDate;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String breed;
    private String homeland;
    @ElementCollection
    private List<String> images;
    private String information;
    @ElementCollection
    private Map<String,String> ancestors;
    private Status status;
    private String reasonOfRejection;
    private LocalDate registrationDate;

    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "horse")
    private List<Like> likes;

}

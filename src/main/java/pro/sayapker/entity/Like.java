package pro.sayapker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(generator = "likes_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "likes_gen", sequenceName = "likes_seq", allocationSize = 1, initialValue = 100)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Horse horse;
}

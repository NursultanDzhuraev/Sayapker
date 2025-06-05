package pro.sayapker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sayapker.entity.User;
import pro.sayapker.exception.NotFoundException;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new NotFoundException("Пользователь с email " + email + " не найден"));
    }
    default User findByIdlOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + " не найден"));
    }
   @Query("select u from User u where u.role = 'USER'")
    Page<User> findAllUsers(Pageable pageable);

    @Query(value = """
select u.* from users u
                  join horses h on u.id = h.user_id
where h.id = :horseId
""", nativeQuery = true)
    User findUserByHorseId(@Param("horseId") Long horseId);
}


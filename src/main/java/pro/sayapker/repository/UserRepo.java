package pro.sayapker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sayapker.entity.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

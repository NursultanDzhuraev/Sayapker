package pro.sayapker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sayapker.entity.Like;

public interface LikesRepo extends JpaRepository<Like,Long> {
}

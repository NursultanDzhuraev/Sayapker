package pro.sayapker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sayapker.entity.Horse;

public interface HorseRepo extends JpaRepository<Horse, Long> {
    @Query(value = "select h.* from horses h where h.status = 'PENDING'", nativeQuery = true)
    Page<Horse> findAllHorse(Pageable pageable);
    @Query(value = "select h.* from horses h where h.id = :horseId and h.status = 'PENDING'",nativeQuery = true)
    Horse findByIdHorse(Long horseId);
    @Query(value = "select h.* from horses h where h.id = :horseId and h.status = 'ACCEPTED'",nativeQuery = true)
    Horse findHorseById(Long horseId);
    @Query(value = "select h.* from horses h where h.id = :horseId and h.status = 'ACCEPTED' and h.user_id = :userId",
            nativeQuery = true)
    Horse findHorseIdAndUserId(Long horseId, Long userId);
}

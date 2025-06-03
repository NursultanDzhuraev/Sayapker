package pro.sayapker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sayapker.entity.Horse;

public interface HorseRepo extends JpaRepository<Horse, Long> {
@Query(value = """
        select h from Horse h where h.status = 'PENDING'
        """,nativeQuery=true)
    Page<Horse> findAllHorse(Pageable pageable);
}

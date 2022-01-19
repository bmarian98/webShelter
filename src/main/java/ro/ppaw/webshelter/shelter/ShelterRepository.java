package ro.ppaw.webshelter.shelter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long>  {

    void deleteShelterById(Long id);

    Optional<Shelter> findShelterById(Long id);

}

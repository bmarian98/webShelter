package ro.ppaw.webshelter.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    void deletePetById(Long id);

    Optional<Pet> findPetById(Long id);

    @Query(" FROM Pet p WHERE p.shelter.id = ?1")
    Optional<List<Pet>>getPetsByShelterId(Long id);
}

package ro.ppaw.webshelter.pet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.ppaw.webshelter.login.LoginController;
import ro.ppaw.webshelter.utility.exception.UserNotFoundException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {
    private final PetRepository petRepo;
    Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public PetService(PetRepository petRepo){
        this.petRepo = petRepo;
    }

    public Pet addPet(Pet pet){
        LOGGER.debug("PET add: " + pet);
        return petRepo.save(pet);
    }

    //@Cacheable("findAllPets")
    public List<Pet> findAllPets(){

        LOGGER.debug("PET findAllPets: was called!");
        return petRepo.findAll();
    }

    public Pet updatePet(Pet pet){

        LOGGER.debug("PET updatePet: " + pet);
        return petRepo.save(pet);
    }

    public void deletePet(Long id){

        LOGGER.debug("PET delete: " + id);
        petRepo.deletePetById(id);
    }

    //@Cacheable(value = "findPetsByShelterId", sync = true)
    public List<Pet> findPetsByShelterId(Long id){
        return petRepo.getPetsByShelterId(id).orElseThrow(() -> new UserNotFoundException(
                "[findPetsByShelterId] Shelter with id: " + id + " doesn't have pets!"));
    }

    //@Cacheable("findPetById")
    public Pet findPetById(Long id){
        return petRepo.findPetById(id).orElseThrow(() -> new UserNotFoundException(
                "[findPetById] Pet with id " + id + " not found!"));
    }
}
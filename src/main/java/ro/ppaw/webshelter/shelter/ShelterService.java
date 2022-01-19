package ro.ppaw.webshelter.shelter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ppaw.webshelter.login.LoginController;
import ro.ppaw.webshelter.utility.exception.UserNotFoundException;

import java.util.List;

@Service
@Transactional
public class ShelterService {
    private final ShelterRepository shelterRepository;

    Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public Shelter addShelter(Shelter shelter){

        LOGGER.debug("[addShelter] : " + shelter);
        return shelterRepository.save(shelter);
    }

    //@Cacheable("getShelters")
    public List<Shelter> getShelters(){

        LOGGER.debug("[getShelters] : called!");
        return  shelterRepository.findAll();
    }

   // @Cacheable("getShelterById")
    public Shelter getShelterById(Long id){
        return shelterRepository.findShelterById(id).orElseThrow(() -> new UserNotFoundException("Shelter with id " + id + " not found!"));
    }

    public Shelter editShelterById(Shelter shelter){

        LOGGER.debug("[editShelterById] : " + shelter);
        return shelterRepository.save(shelter);
    }


    public void delteShelter(Long id){

        LOGGER.debug("[delteShelter] : " + id);
        shelterRepository.deleteShelterById(id);
    }

}

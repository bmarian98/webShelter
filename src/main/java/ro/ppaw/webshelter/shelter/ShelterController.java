package ro.ppaw.webshelter.shelter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ppaw.webshelter.login.LoginController;

import java.util.List;

@RestController
@RequestMapping("/shelter")
@CrossOrigin(origins = "http://localhost:4200/shelter")
public class ShelterController {

    private final ShelterService shelterService;

    Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    public ShelterController(ShelterService shelterService){
        this.shelterService = shelterService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Shelter>> getAllPets(){
        List<Shelter> pets = shelterService.getShelters();

        LOGGER.debug("/shelter/all called succeed!");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Shelter> getPetById(@PathVariable("id") Long id){
        Shelter shelter = shelterService.getShelterById(id);

        LOGGER.debug("/shelter/find/" + id + " call succeed!");
        return new ResponseEntity<>(shelter, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Shelter> addPet(@RequestBody Shelter shelter){
        Shelter newShelter = shelterService.addShelter(shelter);

        LOGGER.debug("/shelter/add/ call succeed! SHELTER: " + newShelter);
        return new ResponseEntity<>(newShelter, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<Shelter> editPet(@RequestBody Shelter shelter){
        Shelter editShelter = shelterService.editShelterById(shelter);

        LOGGER.debug("/shelter/edit/ call succeed! SHELTER: " + editShelter);
        return new ResponseEntity<>(editShelter, HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Shelter> editPetById(@PathVariable("id") Long id, @RequestBody Shelter newShelter){
        Shelter oldShelter = shelterService.getShelterById(id);

        LOGGER.debug("/shelter/edit/" + id + " call succeed! SHELTER: " + oldShelter);

        return new ResponseEntity<>(oldShelter, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id){
        shelterService.delteShelter(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package ro.ppaw.webshelter.pet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ppaw.webshelter.login.LoginController;

import java.util.List;

@RestController
@RequestMapping("/pet")
@CrossOrigin(origins = "http://localhost:4200/pet")
public class PetController {
    private final PetService petService;
    Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    public PetController(PetService petService){
        this.petService = petService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Pet>> getAllPets(){
        List<Pet> pets = petService.findAllPets();

        LOGGER.info("/pet/all call succeed");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<Pet>> getAllPetsFromShelter(@PathVariable("id") Long id){
        List<Pet> pets = petService.findPetsByShelterId(id);

        LOGGER.info("/pet/all/" + id + " call succeed");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable("id") Long id){
        Pet pet = petService.findPetById(id);

        LOGGER.info("/find/" + id + " call succeed");
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet){
        Pet newPet = petService.addPet(pet);

        LOGGER.info("/add call succeed");
        return new ResponseEntity<>(newPet, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<Pet> editPet(@RequestBody Pet pet){
        Pet editPet = petService.updatePet(pet);

        LOGGER.info("/edit call succeed");
        return new ResponseEntity<>(editPet, HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Pet> editPetById(@PathVariable("id") Long id, @RequestBody Pet newPet){
        Pet oldPet = petService.findPetById(id);

        LOGGER.info("/edit/" + id + " call succeed");
        return new ResponseEntity<>(oldPet, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id){
        petService.deletePet(id);

        LOGGER.info("/delete/" + id + " call succeed");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

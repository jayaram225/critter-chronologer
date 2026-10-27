package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertDTOToEntity(petDTO);

        Pet savedPet = petService.savePet(
                pet,
                petDTO.getOwnerId()
        );

        return convertEntityToDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);

        return convertEntityToDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets() {
        return petService.getAllPets()
                .stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(
            @PathVariable long ownerId) {

        return petService.getPetsByOwnerId(ownerId)
                .stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    private Pet convertDTOToEntity(PetDTO petDTO) {
        Pet pet = new Pet();

        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());

        return pet;
    }

    private PetDTO convertEntityToDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();

        petDTO.setId(pet.getId());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());

        if (pet.getOwner() != null) {
            petDTO.setOwnerId(pet.getOwner().getId());
        }

        return petDTO;
    }
}
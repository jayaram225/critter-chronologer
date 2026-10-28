package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PetService {

    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository,
                      CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet savePet(Pet pet, Long ownerId) {
        Customer owner = customerRepository.findById(ownerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: " + ownerId
                        )
                );

        pet.setOwner(owner);
        Pet savedPet = petRepository.save(pet);

        owner.getPets().add(savedPet);

        return savedPet;
    }

    @Transactional(readOnly = true)
    public Pet getPetById(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pet not found with id: " + petId
                        )
                );

        if (pet.getOwner() != null) {
            pet.getOwner().getName();
            pet.getOwner().getPets().size();
        }

        return pet;
    }

    @Transactional(readOnly = true)
    public List<Pet> getAllPets() {
        List<Pet> pets = petRepository.findAll();

        pets.forEach(pet -> {
            if (pet.getOwner() != null) {
                pet.getOwner().getId();
            }
        });

        return pets;
    }

    @Transactional(readOnly = true)
    public List<Pet> getPetsByOwnerId(Long ownerId) {
        if (!customerRepository.existsById(ownerId)) {
            throw new IllegalArgumentException(
                    "Customer not found with id: " + ownerId
            );
        }

        List<Pet> pets = petRepository.findAllByOwnerId(ownerId);

        pets.forEach(pet -> {
            if (pet.getOwner() != null) {
                pet.getOwner().getId();
            }
        });

        return pets;
    }
}
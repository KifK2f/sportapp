package com.monsalon.sportapp.repositories; // Package dédié aux repositories.

import com.monsalon.sportapp.entities.Customer; // Import de l'entité Customer.
import org.springframework.data.jpa.repository.JpaRepository; // Import de JpaRepository.

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Hérite des méthodes CRUD par défaut : save(), findById(), findAll(), deleteById().

    // Rechercher des clients par nom
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);

    // Rechercher des clients par prénom
    List<Customer> findByFirstNameContainingIgnoreCase(String firstName);

    // Rechercher des clients par statut d'abonnement actif
    List<Customer> findByActiveSubscriptionTrue();

    // Rechercher des clients par date d'inscription (à partir d'une date donnée)
    List<Customer> findByRegistrationDateAfter(java.time.LocalDate date);

    // Rechercher des clients par date d'inscription (avant une date donnée)
    List<Customer> findByRegistrationDateBefore(java.time.LocalDate date);
}

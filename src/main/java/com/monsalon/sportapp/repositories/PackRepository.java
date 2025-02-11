package com.monsalon.sportapp.repositories;

import com.monsalon.sportapp.entities.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Annotation pour indiquer qu'il s'agit d'un repository.
public interface PackRepository extends JpaRepository<Pack, Long> {

    // Méthode pour rechercher les packs par nom de l'offre (recherche insensible à la casse).
    List<Pack> findByOfferNameContainingIgnoreCase(String offerName);

    // Méthode pour rechercher les packs avec une durée spécifique.
    List<Pack> findByDurationMonths(Integer durationMonths);

    // Méthode pour rechercher les packs avec un prix mensuel inférieur ou égal à un certain montant.
    List<Pack> findByMonthlyPriceLessThanEqual(Double maxPrice);

    // Méthode pour rechercher les packs dont la durée est supérieure à une certaine valeur.
    List<Pack> findByDurationMonthsGreaterThan(Integer minDuration);

    // Méthode pour rechercher les packs dont le prix est supérieur à un certain montant.
    List<Pack> findByMonthlyPriceGreaterThan(Double minPrice);

    // Méthode pour rechercher les packs triés par prix mensuel, par ordre croissant.
    List<Pack> findByOrderByMonthlyPriceAsc();

    // Méthode pour rechercher les packs triés par durée, par ordre décroissant.
    List<Pack> findByOrderByDurationMonthsDesc();
}

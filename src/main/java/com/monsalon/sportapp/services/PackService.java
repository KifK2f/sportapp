package com.monsalon.sportapp.services;

import com.monsalon.sportapp.entities.Pack;
import com.monsalon.sportapp.repositories.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackService {

    @Autowired
    private PackRepository packRepository;

    // Méthode pour ajouter un nouveau pack.
    public Pack addPack(Pack pack) {
        return packRepository.save(pack);
    }

    // Méthode pour récupérer tous les packs.
    public List<Pack> getAllPacks() {
        return packRepository.findAll();
    }

    // Méthode pour récupérer un pack par son ID.
    public Pack getPackById(Long id) {
        return packRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pack not found with ID: " + id)); // Lève une exception si le pack n'existe pas.
    }

    // Méthode pour rechercher des packs par nom de l'offre.
    public List<Pack> searchPacksByName(String name) {
        return packRepository.findByOfferNameContainingIgnoreCase(name); // Recherche insensible à la casse.
    }

    // Méthode pour rechercher des packs avec une durée spécifique.
    public List<Pack> searchPacksByDuration(Integer durationMonths) {
        return packRepository.findByDurationMonths(durationMonths); // Recherche par durée.
    }

    // Méthode pour rechercher des packs avec un prix mensuel maximum.
    public List<Pack> searchPacksByMaxPrice(Double maxPrice) {
        return packRepository.findByMonthlyPriceLessThanEqual(maxPrice); // Recherche par prix.
    }

    // Méthode pour supprimer un pack par son ID.
    public void deletePack(Long id) {
        if (!packRepository.existsById(id)) {
            throw new RuntimeException("Pack not found with ID: " + id); // Gère les suppressions de packs inexistants.
        }
        packRepository.deleteById(id);
    }

    // Méthode pour mettre à jour un pack existant.
    public Pack updatePack(Long id, Pack updatedPack) {
        return packRepository.findById(id).map(pack -> {
            pack.setOfferName(updatedPack.getOfferName());
            pack.setDurationMonths(updatedPack.getDurationMonths());
            pack.setMonthlyPrice(updatedPack.getMonthlyPrice());
            return packRepository.save(pack);
        }).orElseThrow(() -> new RuntimeException("Pack not found with ID: " + id)); // Lève une exception si le pack n'existe pas.
    }
}

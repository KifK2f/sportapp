package com.monsalon.sportapp.controllers;

import com.monsalon.sportapp.entities.Pack;
import com.monsalon.sportapp.services.PackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/packs")

public class PackController {

    @Autowired
    private PackService packService;

    // Récupérer tous les packs
    @GetMapping
    public List<Pack> getAllPacks() {
        return packService.getAllPacks();
    }

    // Récupérer un pack par ID
    @GetMapping("/{id}")
    public ResponseEntity<Pack> getPackById(@PathVariable Long id) {
        Optional<Pack> pack = Optional.ofNullable(packService.getPackById(id));
        return pack.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Ajouter un nouveau pack
    @PostMapping
    public Pack addPack(@RequestBody Pack pack) {
        return packService.addPack(pack);
    }

    // Mettre à jour un pack existant
    @PutMapping("/{id}")
    public ResponseEntity<Pack> updatePack(@PathVariable Long id, @RequestBody Pack updatedPack) {
        try {
            Pack pack = packService.updatePack(id, updatedPack);
            return ResponseEntity.ok(pack);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un pack par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePack(@PathVariable Long id) {
        try {
            packService.deletePack(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}

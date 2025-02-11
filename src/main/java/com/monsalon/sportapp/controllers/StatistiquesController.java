package com.monsalon.sportapp.controllers;

import com.monsalon.sportapp.services.StatistiquesService;
import com.monsalon.sportapp.services.SubscriptionExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/api/statistics")
public class StatistiquesController {

    private final StatistiquesService statistiquesService;
    private final SubscriptionExportService subscriptionExportService;

    // ✅ Fusion des deux constructeurs
    public StatistiquesController(StatistiquesService statistiquesService, SubscriptionExportService subscriptionExportService) {
        this.statistiquesService = statistiquesService;
        this.subscriptionExportService = subscriptionExportService;
    }

    /**
     * 📌 API pour récupérer le nombre total de clients actifs.
     */
    @GetMapping("/clients-actifs")
    public ResponseEntity<Long> getTotalClientsActifs() {
        Long totalClientsActifs = statistiquesService.getTotalClientsActifs();
        return ResponseEntity.ok(totalClientsActifs);
    }

    /**
     * 📌 API pour récupérer le chiffre d’affaires mensuel estimé.
     */
    @GetMapping("/monthly-revenue")
    public ResponseEntity<Double> getMonthlyRevenue() {
        return ResponseEntity.ok(statistiquesService.getMonthlyRevenue());
    }

    /**
     * 📌 Endpoint pour exporter les abonnements sur une période donnée
     */
    @GetMapping("/export-subscriptions")
    public void exportSubscriptions(
            HttpServletResponse response,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {

        // Définir le type de réponse pour le téléchargement CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=subscriptions.csv");

        // Générer le fichier CSV et l'écrire dans la réponse
        subscriptionExportService.exportSubscriptionsToCsv(response.getWriter(), startDate, endDate);
    }
}

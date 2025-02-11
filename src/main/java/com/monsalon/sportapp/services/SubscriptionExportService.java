package com.monsalon.sportapp.services;

import com.monsalon.sportapp.entities.Subscription;
import com.monsalon.sportapp.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionExportService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionExportService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * 📌 Exporter les abonnements en CSV sur une période donnée
     */
    public void exportSubscriptionsToCsv(PrintWriter writer, Date startDate, Date endDate) {
        // Récupérer les abonnements entre startDate et endDate
        List<Subscription> subscriptions = subscriptionRepository.findByStartDateBetween(startDate, endDate);

        // Format de date pour le CSV
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Écrire l'en-tête du fichier CSV
        writer.println("ID,Client,Offre,Date de début");

        // Écrire chaque abonnement dans le fichier CSV
        for (Subscription subscription : subscriptions) {
            writer.println(subscription.getId() + "," +
                    subscription.getCustomer().getFirstName() + " " + subscription.getCustomer().getLastName() + "," +
                    subscription.getPack().getOfferName() + "," +
                    dateFormat.format(subscription.getStartDate()));
        }
    }
}

package com.monsalon.sportapp.services;

import com.monsalon.sportapp.entities.Subscription;
import com.monsalon.sportapp.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class StatistiquesService {

    private final SubscriptionRepository subscriptionRepository;

    public StatistiquesService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Long getTotalClientsActifs() {
        return subscriptionRepository.countByStatus(Subscription.Status.ACTIVE);
    }

    /**
     * ✅ Retourne le chiffre d’affaires mensuel estimé basé sur les abonnements actifs.
     */
    public Double getMonthlyRevenue() {
        return subscriptionRepository.getTotalRevenueFromActiveSubscriptions(Subscription.Status.ACTIVE);
    }
}

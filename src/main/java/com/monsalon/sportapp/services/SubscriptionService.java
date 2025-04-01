package com.monsalon.sportapp.services;

import com.monsalon.sportapp.entities.Customer;
import com.monsalon.sportapp.entities.Pack;
import com.monsalon.sportapp.entities.Subscription;
import com.monsalon.sportapp.repositories.CustomerRepository;
import com.monsalon.sportapp.repositories.PackRepository;
import com.monsalon.sportapp.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PackRepository packRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               CustomerRepository customerRepository,
                               PackRepository packRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.customerRepository = customerRepository;
        this.packRepository = packRepository;
    }

    // Ajouter un abonnement
    public Subscription addSubscription(Subscription subscription) {
        // Vérifier que la startDate n'est pas après la endDate
        if (subscription.getStartDate().isAfter(subscription.getEndDate())) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin.");
        }
    
        // Vérifier que le client et le pack existent
        Customer customer = customerRepository.findById(subscription.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID : " + subscription.getCustomer().getId()));
    
        Pack pack = packRepository.findById(subscription.getPack().getId())
                .orElseThrow(() -> new RuntimeException("Pack non trouvé avec l'ID : " + subscription.getPack().getId()));
    
        subscription.setCustomer(customer);
        subscription.setPack(pack);
    
        return subscriptionRepository.save(subscription);
    }
    

    // Récupérer tous les abonnements
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    // Récupérer un abonnement par ID
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    // Mettre à jour un abonnement
    public Optional<Subscription> updateSubscription(Long id, Subscription updatedSubscription) {
        return subscriptionRepository.findById(id).map(subscription -> {
            validateDates(updatedSubscription.getStartDate(), updatedSubscription.getEndDate());

            Customer customer = customerRepository.findById(updatedSubscription.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + updatedSubscription.getCustomer().getId()));

            Pack pack = packRepository.findById(updatedSubscription.getPack().getId())
                    .orElseThrow(() -> new RuntimeException("Pack not found with ID: " + updatedSubscription.getPack().getId()));

            subscription.setStartDate(updatedSubscription.getStartDate());
            subscription.setEndDate(updatedSubscription.getEndDate());
            subscription.setStatus(updatedSubscription.getStatus());
            subscription.setCustomer(customer);
            subscription.setPack(pack);

            return subscriptionRepository.save(subscription);
        });
    }

    // Résilier un abonnement
    public Subscription cancelSubscription(Long id) {
        return subscriptionRepository.findById(id).map(subscription -> {
            subscription.setStatus(Subscription.Status.RESILIE);
            return subscriptionRepository.save(subscription);
        }).orElseThrow(() -> new RuntimeException("Subscription not found with ID: " + id));
    }

    // Supprimer un abonnement
    public void deleteSubscription(Long id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Subscription not found with ID: " + id);
        }
        subscriptionRepository.deleteById(id);
    }


    // Validation des dates
    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
    }
}

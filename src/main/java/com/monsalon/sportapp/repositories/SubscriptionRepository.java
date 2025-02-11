package com.monsalon.sportapp.repositories;

import com.monsalon.sportapp.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // Recherche des abonnements par client
    List<Subscription> findByCustomerId(Long customerId);

    // Recherche des abonnements par pack
    List<Subscription> findByPackId(Long packId);

    // Recherche des abonnements par date de début
    List<Subscription> findByStartDate(java.time.LocalDate startDate);

    // Recherche des abonnements expirés
    List<Subscription> findByEndDateBefore(java.time.LocalDate currentDate);

    // Rechercher les abonnements résiliés
    List<Subscription> findByStatus(Subscription.Status status);

    /**
     * ✅ Compte le nombre total d'abonnés ayant un abonnement actif.
     */
    Long countByStatus(Subscription.Status status);

    /**
     * ✅ Correction ici : Utilisation de s.pack.monthlyPrice au lieu de s.pack.price
     * ✅ Ajout de COALESCE(SUM(...), 0) pour éviter de retourner null
     */
    @Query("SELECT COALESCE(SUM(s.pack.monthlyPrice), 0) FROM Subscription s WHERE s.status = :activeStatus")
    Double getTotalRevenueFromActiveSubscriptions(@Param("activeStatus") Subscription.Status activeStatus);

    // 📌 Récupérer les abonnements entre deux dates
    List<Subscription> findByStartDateBetween(Date startDate, Date endDate);

}
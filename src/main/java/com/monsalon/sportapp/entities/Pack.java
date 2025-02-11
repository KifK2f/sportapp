package com.monsalon.sportapp.entities; // Package pour les entités.

import jakarta.persistence.*; // Import des annotations JPA.

@Entity // Annotation pour indiquer que cette classe est une entité JPA.
@Table(name = "packs") // Définit le nom de la table associée dans la base de données.
public class Pack {

    @Id // Indique que ce champ est la clé primaire.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrément de la clé primaire.
    private Long id;

    @Column(name = "offer_name", nullable = false, unique = true) // Colonne pour le nom du pack (obligatoire et unique).
    private String offerName;



    @Column(name = "duration_months", nullable = false) // Colonne pour la durée de l'abonnement en mois.
    private Integer durationMonths;

    @Column(name = "monthly_price", nullable = false) // ✅ Correction de "price" en "monthlyPrice"
    private Double monthlyPrice;

    // Constructeur par défaut requis par JPA.
    public Pack() {
    }

    // Constructeur avec tous les champs sauf l'id (géré par la base de données).
    public Pack(String offerName, String description, Integer durationMonths, Double monthlyPrice) {
        this.offerName = offerName;
        this.durationMonths = durationMonths;
        this.monthlyPrice = monthlyPrice;
    }

    // Getters et setters pour chaque attribut.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }



    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public Double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(Double monthlyPrice) { // ✅ Correction ici
        this.monthlyPrice = monthlyPrice;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "id=" + id +
                ", offerName='" + offerName + '\'' +
                ", durationMonths=" + durationMonths +
                ", monthlyPrice=" + monthlyPrice +
                '}';
    }
}
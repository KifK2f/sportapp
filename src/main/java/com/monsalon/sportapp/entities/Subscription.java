package com.monsalon.sportapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    public enum Status {
        ACTIVE,
        INACTIVE,
        RESILIE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le client est obligatoire.")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @NotNull(message = "Le pack est obligatoire.")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "pack_id", nullable = false)
    private Pack pack;
    

    @NotNull(message = "Start date is required.")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @NotNull(message = "Status is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;



    // 🛠 Constructeurs
    public Subscription() {
    }

    public Subscription(Customer customer, Pack pack, LocalDate startDate, LocalDate endDate, Status status) {
        this.customer = customer;
        this.pack = pack;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // 🛠 Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Pack getPack() {
        if (pack == null) {
            throw new IllegalStateException("Le pack associé à cette souscription est null.");
        }
        return pack;
    }

    public void setPack(Pack pack) {
        if (pack == null) {
            throw new IllegalArgumentException("Le pack ne peut pas être null.");
        }
        this.pack = pack;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;

    }





    // 🛠 hashCode() et equals() pour éviter les problèmes d'identité avec Hibernate
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // 🛠 toString() simplifié pour le debugging
    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", customer=" + (customer != null ? customer.getId() : "null") +
                ", pack=" + (pack != null ? pack.getId() : "null") +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +

                '}';
    }
}
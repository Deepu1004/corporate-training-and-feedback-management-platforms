package com.tnf.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="batch")
public class Batch {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="batch_id",nullable=false)
    private Long batchId;
    @Column(name="batch_name")
    private String batchName;
    private String technology ;
    @Column(name="start_date")
    private LocalDateTime startDate ;
    @Column(name="end_date")
    private LocalDateTime endDate ;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    public Batch() {
    }

    public Batch(String batchName, LocalDateTime endDate, LocalDateTime startDate, String technology, Trainer trainer) {
        this.batchName = batchName;
        this.endDate = endDate;
        this.startDate = startDate;
        this.technology = technology;
        this.trainer = trainer;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }



}


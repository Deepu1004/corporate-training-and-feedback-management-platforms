package com.tnf.entity;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="trainer_Id")
    private Long id;

    @Column(name = "name", nullable = false, length=50)
    private String name;

    @ElementCollection
    private List<String> technology;

    @Column(name = "experience", nullable = false)
    private int experience;
    @Column(name = "email", nullable = false, unique=true, length=50)
    private String email;
    @Column(name = "mobile", nullable = false, unique=true, length=10)
    private String mobile;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainingBatches> t_batches;

//Constructor
    public Trainer(Long id, String name, List<String> technology, int experience, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.technology = technology;
        this.experience = experience;
        this.email = email;
        this.mobile = mobile;
    }
    public Trainer(){}

// Getter Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getTechnology() {
        return technology;
    }
    public void setTechnology(List<String> technology) {
        this.technology = technology;
    }
    public int getExperience() {
        return experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "Trainer [id=" + id + ", name=" + name + ", technology=" + technology + ", experience=" + experience
                + ", email=" + email + ", mobile=" + mobile + "]";
    }
}
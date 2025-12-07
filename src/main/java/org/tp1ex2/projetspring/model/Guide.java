package org.tp1ex2.projetspring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Guide extends User {

    private String bio;

    @ElementCollection
    private List<String> languages = new ArrayList<>();

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tour> toursCreated = new ArrayList<>();

    private Double rating = 0.0;

}

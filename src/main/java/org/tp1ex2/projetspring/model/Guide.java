package org.tp1ex2.projetspring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("GUIDE")
@Getter
@Setter
public class Guide extends User {

    private String bio;

    @ElementCollection
    private List<String> languages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Tour> toursCreated = new ArrayList<>();

    private Double rating = 0.0;

}

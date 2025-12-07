package org.tp1ex2.projetspring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String pictureUrl;

    private String category;

    private Double latitude;

    private Double longitude;

    private String city;

}

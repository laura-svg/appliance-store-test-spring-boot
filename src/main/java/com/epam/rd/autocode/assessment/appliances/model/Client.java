package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client extends User {
    @Column(name = "card")
    private String card;
    public Client(Long id, String name, String email, String password, String card) {
        super(id, name, email, password);
        this.card = card;
    }
}

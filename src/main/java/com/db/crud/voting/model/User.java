package com.db.crud.voting.model;

import java.time.LocalDateTime;

import com.db.crud.voting.enums.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(length = 30, nullable = false)
    private String surname;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(name = "creation_date")
    private final LocalDateTime createdOn = LocalDateTime.now();

    public String getFullname() {
        return this.getFirstName()+" "+this.getSurname();
    }
}

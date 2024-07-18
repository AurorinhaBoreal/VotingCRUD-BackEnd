package com.db.crud.voting.model;

import java.time.LocalDateTime;

import com.db.crud.voting.enums.Operation;

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
@Entity(name = "tbl_log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long id;

    @Column(name = "object_type", nullable = false)
    private String objectType;

    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @Column(nullable = false)
    private String objectInfo;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime realizedOn = LocalDateTime.now();
}

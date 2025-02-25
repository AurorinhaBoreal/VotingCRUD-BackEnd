package com.db.crud.voting.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import com.db.crud.voting.enums.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Entity(name = "tbl_agenda")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agenda")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(length = 200)
    private String question;

    @Column(name = "yes_votes")
    @Builder.Default
    private Integer yesVotes = 0;

    @Column(name = "no_votes")
    @Builder.Default
    private Integer noVotes = 0;

    @Column(name = "total_votes")
    @Builder.Default
    private Integer totalVotes = 0;

    @Column(name = "duration_minutes")
    @Builder.Default
    private Integer duration = 1;

    @Column(name = "users_voted")
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "tbl_agenda_users_voted",
        joinColumns = @JoinColumn(name = "tbl_agenda_id_agenda"),
        inverseJoinColumns = @JoinColumn(name = "users_voted_id_user")
    )
    private List<User> usersVoted = new ArrayList<>();

    @Column(name = "agenda_ended")
    @Builder.Default
    private boolean hasEnded = false;

    @Column(name = "creation_date")
    @Builder.Default
    private final LocalDateTime createdOn = (LocalDateTime.now()).truncatedTo(ChronoUnit.SECONDS);

    @Column(name = "finish_date")
    @Builder.Default
    private final LocalDateTime finishOn = LocalDateTime.now().plusMinutes(1);
}

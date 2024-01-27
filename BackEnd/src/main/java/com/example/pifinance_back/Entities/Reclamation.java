package com.example.pifinance_back.Entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    private LocalDate date;
    private String sujet;
    private String email;
    private String description;
    @Enumerated(EnumType.STRING)
    private statusReclamation status;
    private Date completionTime; // New field for completion time
    private boolean archived; // New field for archiving

    // Generate getter and setter methods for the 'archived' field
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    // Generate getter and setter methods for the 'completionTime' field
    public Date getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

}

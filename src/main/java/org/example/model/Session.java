package org.example.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sessions")
@Getter
@Setter
public class Session {

    @Id
    private String id;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expires_at")
    private Timestamp expiresAt;


}

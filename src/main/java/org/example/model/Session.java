package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@AllArgsConstructor()
@NoArgsConstructor
public class Session {

    @Id
    private String id;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expires_at")
    private Timestamp expiresAt;



}

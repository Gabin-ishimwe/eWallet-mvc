package com.ewallet.userService.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "first_name"
    )
    private String firstName;

    @Column(
            name = "last_name"
    )
    private String lastName;

    @Column(
            name = "email"
    )
    private String email;

    @Column(
            name = "password"
    )
    private String password;

    @Column(
            name = "contact_number"
    )
    private String contactNumber;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "user_role_mapping",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private List<Role> roles;

}
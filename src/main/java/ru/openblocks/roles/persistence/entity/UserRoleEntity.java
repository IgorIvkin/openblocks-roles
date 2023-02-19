package ru.openblocks.roles.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "user_name")
    private String userName;

    @ManyToOne
    @JoinColumn(name = "role_code")
    private RoleEntity roleCode;

    @Column(name = "created_at")
    private Instant createdAt;

    @NotNull
    @Size(max = 255)
    @Column(name = "grant_by")
    private String grantBy;
}

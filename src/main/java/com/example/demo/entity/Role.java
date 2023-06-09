package com.example.demo.entity;

import com.example.demo.type.RoleSet;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleSet name;

    @ManyToMany
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    @JoinTable(name = "user_role",
            joinColumns = {
                    @JoinColumn(name = "role_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id")
            }
    )
    @ToString.Exclude
    private transient Set<User> users;

    @Override
    public String getAuthority() {
        return name.name();
    }

}

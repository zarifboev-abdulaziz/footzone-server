package com.footzone.footzone.entity.user;


import com.footzone.footzone.entity.attachment.Attachment;
import com.footzone.footzone.entity.role.Role;
import com.footzone.footzone.enums.Language;
import com.footzone.footzone.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "users")
public class User extends AbsEntity implements UserDetails {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String smsCode;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;


    @OneToOne(cascade = CascadeType.ALL)
    private Attachment photo;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Language language;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.smsCode;
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public User(String fullName, String phoneNumber, String smsCode, Set<Role> roles, boolean enabled, Attachment photo) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
        this.roles = roles;
        this.enabled = enabled;
        this.photo = photo;
    }
}

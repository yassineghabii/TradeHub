package com.example.pifinance_back.Entities;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client implements Serializable, UserDetails {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastname;
    @Column(name = "id_admin")
    @JsonProperty("id_admin")
    private String id_admin;
    private String firstname;
    private String name;

    @Column(nullable = false)
    private Boolean isAuthenticated = false;
    public void setToken(String token) {
        this.token = token;
        this.isAuthenticated = token != null && !token.trim().isEmpty();
    }
    public Boolean getIsAuthenticated() {
        return token != null && !token.trim().isEmpty();
    }
    @PrePersist
    @PreUpdate
    public void updateIsAuthenticated() {
        this.isAuthenticated = token != null && !token.trim().isEmpty();
    }

    @Column(unique = true)
    private String cin;
    @Column(unique = true) //
    private String phone_number;
    private String address;
    @JsonProperty("email")
    @Email
    @Column(unique = true)
    private String email;
    @JsonProperty("pwd_user")
    private String pwd_user;
    @JsonProperty("role")
    @Enumerated(EnumType.STRING)
    private UserEnum role;
    @Column(columnDefinition = "TEXT")
    private String token;
    @Column(name = "image", length = 5000000) // ajustez la taille selon vos besoins
    private byte[] image;

    public void setImageData(byte[] imageData) {
        this.image = imageData;
    }
    @JsonIgnore
    @Transient // Et ajoutez-la pour cette méthode aussi
    public byte[] getImageData() {
        return image;
    }
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
    @Transient
    private String imageBase64;

    @Override
    public String getPassword() {
        return pwd_user;
    }
    @Override
    public String getUsername() {
        return email;
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

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
        this.name = this.lastname;
    }
    @OneToMany(mappedBy = "client")
    private List<OrdreAchat> ordresAchat;
    @OneToMany(mappedBy = "client")
    private List<OrdreVente> ordreVentes;

    @ManyToMany
    private Set<Formation> formations;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "client")
    private FinancialProfile financialProfile;


}





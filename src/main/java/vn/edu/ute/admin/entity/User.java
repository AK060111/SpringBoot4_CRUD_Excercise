package vn.edu.ute.admin.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
@Entity @Table(name="app_users") public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)     private Long id;
    @Column(nullable=false, unique=true, length=50)     private String username;
    @Column(nullable=false, unique=true, length=254)     private String email;
    @Nationalized @Column(nullable=false, length=100)     private String fullname;
    @Column(nullable=false, length=100)     private String password;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=10)     private Role role;
    public Long getId() {
        return id;
    }
    public void setId(Long value) {
        this.id=value;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String value) {
        this.username=value;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String value) {
        this.email=value;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String value) {
        this.fullname=value;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String value) {
        this.password=value;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role value) {
        this.role=value;
    }
}

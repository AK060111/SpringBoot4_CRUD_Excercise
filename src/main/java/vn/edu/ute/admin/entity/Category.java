package vn.edu.ute.admin.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
@Entity @Table(name="categories") public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)     private Long id;
    @Nationalized @Column(nullable=false, length=100)     private String name;
    public Long getId() {
        return id;
    }
    public void setId(Long value) {
        this.id=value;
    }
    public String getName() {
        return name;
    }
    public void setName(String value) {
        this.name=value;
    }
}

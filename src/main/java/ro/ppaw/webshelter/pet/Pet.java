package ro.ppaw.webshelter.pet;

import org.hibernate.annotations.*;
import ro.ppaw.webshelter.shelter.Shelter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SQLDelete(sql = "UPDATE pet SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Pet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name;
    private String species;
    private String dateBirth;
    private Character sex;
    private String imageUrl;
    private boolean deleted = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "shelter_id"
    )
    private Shelter shelter;

    public Pet() {}

    public Pet(Long id, String name, String species, String dateBirth, String imageUrl, Character sex, Shelter shelter) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.dateBirth = dateBirth;
        this.imageUrl = imageUrl;
        this.sex = sex;
        this.shelter = shelter;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", dateBirth='" + dateBirth + '\'' +
                ", sex=" + sex +
                '}';
    }
}
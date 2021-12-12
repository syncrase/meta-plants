package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Species
 */
@ApiModel(description = "Species")
@Entity
@Table(name = "espece")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Espece implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_fr", nullable = false)
    private String nomFr;

    @Column(name = "nom_latin")
    private String nomLatin;

    @OneToMany(mappedBy = "espece")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "varietes", "synonymes", "espece", "sousEspece" }, allowSetters = true)
    private Set<SousEspece> sousEspeces = new HashSet<>();

    @OneToMany(mappedBy = "espece")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousEspeces", "synonymes", "sousSection", "espece" }, allowSetters = true)
    private Set<Espece> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "especes", "synonymes", "section", "sousSection" }, allowSetters = true)
    private SousSection sousSection;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousEspeces", "synonymes", "sousSection", "espece" }, allowSetters = true)
    private Espece espece;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Espece id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Espece nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Espece nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousEspece> getSousEspeces() {
        return this.sousEspeces;
    }

    public void setSousEspeces(Set<SousEspece> sousEspeces) {
        if (this.sousEspeces != null) {
            this.sousEspeces.forEach(i -> i.setEspece(null));
        }
        if (sousEspeces != null) {
            sousEspeces.forEach(i -> i.setEspece(this));
        }
        this.sousEspeces = sousEspeces;
    }

    public Espece sousEspeces(Set<SousEspece> sousEspeces) {
        this.setSousEspeces(sousEspeces);
        return this;
    }

    public Espece addSousEspeces(SousEspece sousEspece) {
        this.sousEspeces.add(sousEspece);
        sousEspece.setEspece(this);
        return this;
    }

    public Espece removeSousEspeces(SousEspece sousEspece) {
        this.sousEspeces.remove(sousEspece);
        sousEspece.setEspece(null);
        return this;
    }

    public Set<Espece> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Espece> especes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setEspece(null));
        }
        if (especes != null) {
            especes.forEach(i -> i.setEspece(this));
        }
        this.synonymes = especes;
    }

    public Espece synonymes(Set<Espece> especes) {
        this.setSynonymes(especes);
        return this;
    }

    public Espece addSynonymes(Espece espece) {
        this.synonymes.add(espece);
        espece.setEspece(this);
        return this;
    }

    public Espece removeSynonymes(Espece espece) {
        this.synonymes.remove(espece);
        espece.setEspece(null);
        return this;
    }

    public SousSection getSousSection() {
        return this.sousSection;
    }

    public void setSousSection(SousSection sousSection) {
        this.sousSection = sousSection;
    }

    public Espece sousSection(SousSection sousSection) {
        this.setSousSection(sousSection);
        return this;
    }

    public Espece getEspece() {
        return this.espece;
    }

    public void setEspece(Espece espece) {
        this.espece = espece;
    }

    public Espece espece(Espece espece) {
        this.setEspece(espece);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Espece)) {
            return false;
        }
        return id != null && id.equals(((Espece) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Espece{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}

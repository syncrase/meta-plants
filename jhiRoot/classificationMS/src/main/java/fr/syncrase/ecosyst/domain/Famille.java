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
 * Familia
 */
@ApiModel(description = "Familia")
@Entity
@Table(name = "famille")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Famille implements Serializable {

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

    @OneToMany(mappedBy = "famille")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tribuses", "synonymes", "famille", "sousFamille" }, allowSetters = true)
    private Set<SousFamille> sousFamilles = new HashSet<>();

    @OneToMany(mappedBy = "famille")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousFamilles", "synonymes", "superFamille", "famille" }, allowSetters = true)
    private Set<Famille> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "familles", "synonymes", "microOrdre", "superFamille" }, allowSetters = true)
    private SuperFamille superFamille;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousFamilles", "synonymes", "superFamille", "famille" }, allowSetters = true)
    private Famille famille;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Famille id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Famille nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Famille nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousFamille> getSousFamilles() {
        return this.sousFamilles;
    }

    public void setSousFamilles(Set<SousFamille> sousFamilles) {
        if (this.sousFamilles != null) {
            this.sousFamilles.forEach(i -> i.setFamille(null));
        }
        if (sousFamilles != null) {
            sousFamilles.forEach(i -> i.setFamille(this));
        }
        this.sousFamilles = sousFamilles;
    }

    public Famille sousFamilles(Set<SousFamille> sousFamilles) {
        this.setSousFamilles(sousFamilles);
        return this;
    }

    public Famille addSousFamilles(SousFamille sousFamille) {
        this.sousFamilles.add(sousFamille);
        sousFamille.setFamille(this);
        return this;
    }

    public Famille removeSousFamilles(SousFamille sousFamille) {
        this.sousFamilles.remove(sousFamille);
        sousFamille.setFamille(null);
        return this;
    }

    public Set<Famille> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Famille> familles) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setFamille(null));
        }
        if (familles != null) {
            familles.forEach(i -> i.setFamille(this));
        }
        this.synonymes = familles;
    }

    public Famille synonymes(Set<Famille> familles) {
        this.setSynonymes(familles);
        return this;
    }

    public Famille addSynonymes(Famille famille) {
        this.synonymes.add(famille);
        famille.setFamille(this);
        return this;
    }

    public Famille removeSynonymes(Famille famille) {
        this.synonymes.remove(famille);
        famille.setFamille(null);
        return this;
    }

    public SuperFamille getSuperFamille() {
        return this.superFamille;
    }

    public void setSuperFamille(SuperFamille superFamille) {
        this.superFamille = superFamille;
    }

    public Famille superFamille(SuperFamille superFamille) {
        this.setSuperFamille(superFamille);
        return this;
    }

    public Famille getFamille() {
        return this.famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    public Famille famille(Famille famille) {
        this.setFamille(famille);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Famille)) {
            return false;
        }
        return id != null && id.equals(((Famille) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Famille{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}

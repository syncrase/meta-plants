package fr.syncrase.ecosyst.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Cronquist.
 */
@Table("cronquist")
public class Cronquist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("regne")
    private String regne;

    @Column("sous_regne")
    private String sousRegne;

    @Column("division")
    private String division;

    @Column("classe")
    private String classe;

    @Column("sous_classe")
    private String sousClasse;

    @Column("ordre")
    private String ordre;

    @Column("famille")
    private String famille;

    @Column("genre")
    private String genre;

    @Column("espece")
    private String espece;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cronquist id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegne() {
        return this.regne;
    }

    public Cronquist regne(String regne) {
        this.setRegne(regne);
        return this;
    }

    public void setRegne(String regne) {
        this.regne = regne;
    }

    public String getSousRegne() {
        return this.sousRegne;
    }

    public Cronquist sousRegne(String sousRegne) {
        this.setSousRegne(sousRegne);
        return this;
    }

    public void setSousRegne(String sousRegne) {
        this.sousRegne = sousRegne;
    }

    public String getDivision() {
        return this.division;
    }

    public Cronquist division(String division) {
        this.setDivision(division);
        return this;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getClasse() {
        return this.classe;
    }

    public Cronquist classe(String classe) {
        this.setClasse(classe);
        return this;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getSousClasse() {
        return this.sousClasse;
    }

    public Cronquist sousClasse(String sousClasse) {
        this.setSousClasse(sousClasse);
        return this;
    }

    public void setSousClasse(String sousClasse) {
        this.sousClasse = sousClasse;
    }

    public String getOrdre() {
        return this.ordre;
    }

    public Cronquist ordre(String ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getFamille() {
        return this.famille;
    }

    public Cronquist famille(String famille) {
        this.setFamille(famille);
        return this;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public String getGenre() {
        return this.genre;
    }

    public Cronquist genre(String genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEspece() {
        return this.espece;
    }

    public Cronquist espece(String espece) {
        this.setEspece(espece);
        return this;
    }

    public void setEspece(String espece) {
        this.espece = espece;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cronquist)) {
            return false;
        }
        return id != null && id.equals(((Cronquist) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cronquist{" +
            "id=" + getId() +
            ", regne='" + getRegne() + "'" +
            ", sousRegne='" + getSousRegne() + "'" +
            ", division='" + getDivision() + "'" +
            ", classe='" + getClasse() + "'" +
            ", sousClasse='" + getSousClasse() + "'" +
            ", ordre='" + getOrdre() + "'" +
            ", famille='" + getFamille() + "'" +
            ", genre='" + getGenre() + "'" +
            ", espece='" + getEspece() + "'" +
            "}";
    }
}
package fr.syncrase.ecosyst.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entité partagé pour communiquer sur l'arborescence
 */
@ApiModel(description = "Entité partagé pour communiquer sur l'arborescence")
@Entity
@Table(name = "cronquist_plante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CronquistPlante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "super_regne")
    private String superRegne;

    @Column(name = "regne")
    private String regne;

    @Column(name = "sous_regne")
    private String sousRegne;

    @Column(name = "rameau")
    private String rameau;

    @Column(name = "infra_regne")
    private String infraRegne;

    @Column(name = "super_division")
    private String superDivision;

    @Column(name = "division")
    private String division;

    @Column(name = "sous_division")
    private String sousDivision;

    @Column(name = "infra_embranchement")
    private String infraEmbranchement;

    @Column(name = "micro_embranchement")
    private String microEmbranchement;

    @Column(name = "super_classe")
    private String superClasse;

    @Column(name = "classe")
    private String classe;

    @Column(name = "sous_classe")
    private String sousClasse;

    @Column(name = "infra_classe")
    private String infraClasse;

    @Column(name = "super_ordre")
    private String superOrdre;

    @Column(name = "ordre")
    private String ordre;

    @Column(name = "sous_ordre")
    private String sousOrdre;

    @Column(name = "infra_ordre")
    private String infraOrdre;

    @Column(name = "micro_ordre")
    private String microOrdre;

    @Column(name = "super_famille")
    private String superFamille;

    @Column(name = "famille")
    private String famille;

    @Column(name = "sous_famille")
    private String sousFamille;

    @Column(name = "tribu")
    private String tribu;

    @Column(name = "sous_tribu")
    private String sousTribu;

    @Column(name = "genre")
    private String genre;

    @Column(name = "sous_genre")
    private String sousGenre;

    @Column(name = "section")
    private String section;

    @Column(name = "sous_section")
    private String sousSection;

    @Column(name = "espece")
    private String espece;

    @Column(name = "sous_espece")
    private String sousEspece;

    @Column(name = "variete")
    private String variete;

    @Column(name = "sous_variete")
    private String sousVariete;

    @Column(name = "forme")
    private String forme;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CronquistPlante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuperRegne() {
        return this.superRegne;
    }

    public CronquistPlante superRegne(String superRegne) {
        this.setSuperRegne(superRegne);
        return this;
    }

    public void setSuperRegne(String superRegne) {
        this.superRegne = superRegne;
    }

    public String getRegne() {
        return this.regne;
    }

    public CronquistPlante regne(String regne) {
        this.setRegne(regne);
        return this;
    }

    public void setRegne(String regne) {
        this.regne = regne;
    }

    public String getSousRegne() {
        return this.sousRegne;
    }

    public CronquistPlante sousRegne(String sousRegne) {
        this.setSousRegne(sousRegne);
        return this;
    }

    public void setSousRegne(String sousRegne) {
        this.sousRegne = sousRegne;
    }

    public String getRameau() {
        return this.rameau;
    }

    public CronquistPlante rameau(String rameau) {
        this.setRameau(rameau);
        return this;
    }

    public void setRameau(String rameau) {
        this.rameau = rameau;
    }

    public String getInfraRegne() {
        return this.infraRegne;
    }

    public CronquistPlante infraRegne(String infraRegne) {
        this.setInfraRegne(infraRegne);
        return this;
    }

    public void setInfraRegne(String infraRegne) {
        this.infraRegne = infraRegne;
    }

    public String getSuperDivision() {
        return this.superDivision;
    }

    public CronquistPlante superDivision(String superDivision) {
        this.setSuperDivision(superDivision);
        return this;
    }

    public void setSuperDivision(String superDivision) {
        this.superDivision = superDivision;
    }

    public String getDivision() {
        return this.division;
    }

    public CronquistPlante division(String division) {
        this.setDivision(division);
        return this;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSousDivision() {
        return this.sousDivision;
    }

    public CronquistPlante sousDivision(String sousDivision) {
        this.setSousDivision(sousDivision);
        return this;
    }

    public void setSousDivision(String sousDivision) {
        this.sousDivision = sousDivision;
    }

    public String getInfraEmbranchement() {
        return this.infraEmbranchement;
    }

    public CronquistPlante infraEmbranchement(String infraEmbranchement) {
        this.setInfraEmbranchement(infraEmbranchement);
        return this;
    }

    public void setInfraEmbranchement(String infraEmbranchement) {
        this.infraEmbranchement = infraEmbranchement;
    }

    public String getMicroEmbranchement() {
        return this.microEmbranchement;
    }

    public CronquistPlante microEmbranchement(String microEmbranchement) {
        this.setMicroEmbranchement(microEmbranchement);
        return this;
    }

    public void setMicroEmbranchement(String microEmbranchement) {
        this.microEmbranchement = microEmbranchement;
    }

    public String getSuperClasse() {
        return this.superClasse;
    }

    public CronquistPlante superClasse(String superClasse) {
        this.setSuperClasse(superClasse);
        return this;
    }

    public void setSuperClasse(String superClasse) {
        this.superClasse = superClasse;
    }

    public String getClasse() {
        return this.classe;
    }

    public CronquistPlante classe(String classe) {
        this.setClasse(classe);
        return this;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getSousClasse() {
        return this.sousClasse;
    }

    public CronquistPlante sousClasse(String sousClasse) {
        this.setSousClasse(sousClasse);
        return this;
    }

    public void setSousClasse(String sousClasse) {
        this.sousClasse = sousClasse;
    }

    public String getInfraClasse() {
        return this.infraClasse;
    }

    public CronquistPlante infraClasse(String infraClasse) {
        this.setInfraClasse(infraClasse);
        return this;
    }

    public void setInfraClasse(String infraClasse) {
        this.infraClasse = infraClasse;
    }

    public String getSuperOrdre() {
        return this.superOrdre;
    }

    public CronquistPlante superOrdre(String superOrdre) {
        this.setSuperOrdre(superOrdre);
        return this;
    }

    public void setSuperOrdre(String superOrdre) {
        this.superOrdre = superOrdre;
    }

    public String getOrdre() {
        return this.ordre;
    }

    public CronquistPlante ordre(String ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getSousOrdre() {
        return this.sousOrdre;
    }

    public CronquistPlante sousOrdre(String sousOrdre) {
        this.setSousOrdre(sousOrdre);
        return this;
    }

    public void setSousOrdre(String sousOrdre) {
        this.sousOrdre = sousOrdre;
    }

    public String getInfraOrdre() {
        return this.infraOrdre;
    }

    public CronquistPlante infraOrdre(String infraOrdre) {
        this.setInfraOrdre(infraOrdre);
        return this;
    }

    public void setInfraOrdre(String infraOrdre) {
        this.infraOrdre = infraOrdre;
    }

    public String getMicroOrdre() {
        return this.microOrdre;
    }

    public CronquistPlante microOrdre(String microOrdre) {
        this.setMicroOrdre(microOrdre);
        return this;
    }

    public void setMicroOrdre(String microOrdre) {
        this.microOrdre = microOrdre;
    }

    public String getSuperFamille() {
        return this.superFamille;
    }

    public CronquistPlante superFamille(String superFamille) {
        this.setSuperFamille(superFamille);
        return this;
    }

    public void setSuperFamille(String superFamille) {
        this.superFamille = superFamille;
    }

    public String getFamille() {
        return this.famille;
    }

    public CronquistPlante famille(String famille) {
        this.setFamille(famille);
        return this;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public String getSousFamille() {
        return this.sousFamille;
    }

    public CronquistPlante sousFamille(String sousFamille) {
        this.setSousFamille(sousFamille);
        return this;
    }

    public void setSousFamille(String sousFamille) {
        this.sousFamille = sousFamille;
    }

    public String getTribu() {
        return this.tribu;
    }

    public CronquistPlante tribu(String tribu) {
        this.setTribu(tribu);
        return this;
    }

    public void setTribu(String tribu) {
        this.tribu = tribu;
    }

    public String getSousTribu() {
        return this.sousTribu;
    }

    public CronquistPlante sousTribu(String sousTribu) {
        this.setSousTribu(sousTribu);
        return this;
    }

    public void setSousTribu(String sousTribu) {
        this.sousTribu = sousTribu;
    }

    public String getGenre() {
        return this.genre;
    }

    public CronquistPlante genre(String genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSousGenre() {
        return this.sousGenre;
    }

    public CronquistPlante sousGenre(String sousGenre) {
        this.setSousGenre(sousGenre);
        return this;
    }

    public void setSousGenre(String sousGenre) {
        this.sousGenre = sousGenre;
    }

    public String getSection() {
        return this.section;
    }

    public CronquistPlante section(String section) {
        this.setSection(section);
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSousSection() {
        return this.sousSection;
    }

    public CronquistPlante sousSection(String sousSection) {
        this.setSousSection(sousSection);
        return this;
    }

    public void setSousSection(String sousSection) {
        this.sousSection = sousSection;
    }

    public String getEspece() {
        return this.espece;
    }

    public CronquistPlante espece(String espece) {
        this.setEspece(espece);
        return this;
    }

    public void setEspece(String espece) {
        this.espece = espece;
    }

    public String getSousEspece() {
        return this.sousEspece;
    }

    public CronquistPlante sousEspece(String sousEspece) {
        this.setSousEspece(sousEspece);
        return this;
    }

    public void setSousEspece(String sousEspece) {
        this.sousEspece = sousEspece;
    }

    public String getVariete() {
        return this.variete;
    }

    public CronquistPlante variete(String variete) {
        this.setVariete(variete);
        return this;
    }

    public void setVariete(String variete) {
        this.variete = variete;
    }

    public String getSousVariete() {
        return this.sousVariete;
    }

    public CronquistPlante sousVariete(String sousVariete) {
        this.setSousVariete(sousVariete);
        return this;
    }

    public void setSousVariete(String sousVariete) {
        this.sousVariete = sousVariete;
    }

    public String getForme() {
        return this.forme;
    }

    public CronquistPlante forme(String forme) {
        this.setForme(forme);
        return this;
    }

    public void setForme(String forme) {
        this.forme = forme;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CronquistPlante)) {
            return false;
        }
        return id != null && id.equals(((CronquistPlante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CronquistPlante{" +
            "id=" + getId() +
            ", superRegne='" + getSuperRegne() + "'" +
            ", regne='" + getRegne() + "'" +
            ", sousRegne='" + getSousRegne() + "'" +
            ", rameau='" + getRameau() + "'" +
            ", infraRegne='" + getInfraRegne() + "'" +
            ", superDivision='" + getSuperDivision() + "'" +
            ", division='" + getDivision() + "'" +
            ", sousDivision='" + getSousDivision() + "'" +
            ", infraEmbranchement='" + getInfraEmbranchement() + "'" +
            ", microEmbranchement='" + getMicroEmbranchement() + "'" +
            ", superClasse='" + getSuperClasse() + "'" +
            ", classe='" + getClasse() + "'" +
            ", sousClasse='" + getSousClasse() + "'" +
            ", infraClasse='" + getInfraClasse() + "'" +
            ", superOrdre='" + getSuperOrdre() + "'" +
            ", ordre='" + getOrdre() + "'" +
            ", sousOrdre='" + getSousOrdre() + "'" +
            ", infraOrdre='" + getInfraOrdre() + "'" +
            ", microOrdre='" + getMicroOrdre() + "'" +
            ", superFamille='" + getSuperFamille() + "'" +
            ", famille='" + getFamille() + "'" +
            ", sousFamille='" + getSousFamille() + "'" +
            ", tribu='" + getTribu() + "'" +
            ", sousTribu='" + getSousTribu() + "'" +
            ", genre='" + getGenre() + "'" +
            ", sousGenre='" + getSousGenre() + "'" +
            ", section='" + getSection() + "'" +
            ", sousSection='" + getSousSection() + "'" +
            ", espece='" + getEspece() + "'" +
            ", sousEspece='" + getSousEspece() + "'" +
            ", variete='" + getVariete() + "'" +
            ", sousVariete='" + getSousVariete() + "'" +
            ", forme='" + getForme() + "'" +
            "}";
    }
}

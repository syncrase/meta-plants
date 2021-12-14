package fr.syncrase.ecosyst.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.CronquistPlante} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.CronquistPlanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cronquist-plantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CronquistPlanteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter superRegne;

    private StringFilter regne;

    private StringFilter sousRegne;

    private StringFilter rameau;

    private StringFilter infraRegne;

    private StringFilter superDivision;

    private StringFilter division;

    private StringFilter sousDivision;

    private StringFilter infraEmbranchement;

    private StringFilter microEmbranchement;

    private StringFilter superClasse;

    private StringFilter classe;

    private StringFilter sousClasse;

    private StringFilter infraClasse;

    private StringFilter superOrdre;

    private StringFilter ordre;

    private StringFilter sousOrdre;

    private StringFilter infraOrdre;

    private StringFilter microOrdre;

    private StringFilter superFamille;

    private StringFilter famille;

    private StringFilter sousFamille;

    private StringFilter tribu;

    private StringFilter sousTribu;

    private StringFilter genre;

    private StringFilter sousGenre;

    private StringFilter section;

    private StringFilter sousSection;

    private StringFilter espece;

    private StringFilter sousEspece;

    private StringFilter variete;

    private StringFilter sousVariete;

    private StringFilter forme;

    private Boolean distinct;

    public CronquistPlanteCriteria() {}

    public CronquistPlanteCriteria(CronquistPlanteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.superRegne = other.superRegne == null ? null : other.superRegne.copy();
        this.regne = other.regne == null ? null : other.regne.copy();
        this.sousRegne = other.sousRegne == null ? null : other.sousRegne.copy();
        this.rameau = other.rameau == null ? null : other.rameau.copy();
        this.infraRegne = other.infraRegne == null ? null : other.infraRegne.copy();
        this.superDivision = other.superDivision == null ? null : other.superDivision.copy();
        this.division = other.division == null ? null : other.division.copy();
        this.sousDivision = other.sousDivision == null ? null : other.sousDivision.copy();
        this.infraEmbranchement = other.infraEmbranchement == null ? null : other.infraEmbranchement.copy();
        this.microEmbranchement = other.microEmbranchement == null ? null : other.microEmbranchement.copy();
        this.superClasse = other.superClasse == null ? null : other.superClasse.copy();
        this.classe = other.classe == null ? null : other.classe.copy();
        this.sousClasse = other.sousClasse == null ? null : other.sousClasse.copy();
        this.infraClasse = other.infraClasse == null ? null : other.infraClasse.copy();
        this.superOrdre = other.superOrdre == null ? null : other.superOrdre.copy();
        this.ordre = other.ordre == null ? null : other.ordre.copy();
        this.sousOrdre = other.sousOrdre == null ? null : other.sousOrdre.copy();
        this.infraOrdre = other.infraOrdre == null ? null : other.infraOrdre.copy();
        this.microOrdre = other.microOrdre == null ? null : other.microOrdre.copy();
        this.superFamille = other.superFamille == null ? null : other.superFamille.copy();
        this.famille = other.famille == null ? null : other.famille.copy();
        this.sousFamille = other.sousFamille == null ? null : other.sousFamille.copy();
        this.tribu = other.tribu == null ? null : other.tribu.copy();
        this.sousTribu = other.sousTribu == null ? null : other.sousTribu.copy();
        this.genre = other.genre == null ? null : other.genre.copy();
        this.sousGenre = other.sousGenre == null ? null : other.sousGenre.copy();
        this.section = other.section == null ? null : other.section.copy();
        this.sousSection = other.sousSection == null ? null : other.sousSection.copy();
        this.espece = other.espece == null ? null : other.espece.copy();
        this.sousEspece = other.sousEspece == null ? null : other.sousEspece.copy();
        this.variete = other.variete == null ? null : other.variete.copy();
        this.sousVariete = other.sousVariete == null ? null : other.sousVariete.copy();
        this.forme = other.forme == null ? null : other.forme.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CronquistPlanteCriteria copy() {
        return new CronquistPlanteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSuperRegne() {
        return superRegne;
    }

    public StringFilter superRegne() {
        if (superRegne == null) {
            superRegne = new StringFilter();
        }
        return superRegne;
    }

    public void setSuperRegne(StringFilter superRegne) {
        this.superRegne = superRegne;
    }

    public StringFilter getRegne() {
        return regne;
    }

    public StringFilter regne() {
        if (regne == null) {
            regne = new StringFilter();
        }
        return regne;
    }

    public void setRegne(StringFilter regne) {
        this.regne = regne;
    }

    public StringFilter getSousRegne() {
        return sousRegne;
    }

    public StringFilter sousRegne() {
        if (sousRegne == null) {
            sousRegne = new StringFilter();
        }
        return sousRegne;
    }

    public void setSousRegne(StringFilter sousRegne) {
        this.sousRegne = sousRegne;
    }

    public StringFilter getRameau() {
        return rameau;
    }

    public StringFilter rameau() {
        if (rameau == null) {
            rameau = new StringFilter();
        }
        return rameau;
    }

    public void setRameau(StringFilter rameau) {
        this.rameau = rameau;
    }

    public StringFilter getInfraRegne() {
        return infraRegne;
    }

    public StringFilter infraRegne() {
        if (infraRegne == null) {
            infraRegne = new StringFilter();
        }
        return infraRegne;
    }

    public void setInfraRegne(StringFilter infraRegne) {
        this.infraRegne = infraRegne;
    }

    public StringFilter getSuperDivision() {
        return superDivision;
    }

    public StringFilter superDivision() {
        if (superDivision == null) {
            superDivision = new StringFilter();
        }
        return superDivision;
    }

    public void setSuperDivision(StringFilter superDivision) {
        this.superDivision = superDivision;
    }

    public StringFilter getDivision() {
        return division;
    }

    public StringFilter division() {
        if (division == null) {
            division = new StringFilter();
        }
        return division;
    }

    public void setDivision(StringFilter division) {
        this.division = division;
    }

    public StringFilter getSousDivision() {
        return sousDivision;
    }

    public StringFilter sousDivision() {
        if (sousDivision == null) {
            sousDivision = new StringFilter();
        }
        return sousDivision;
    }

    public void setSousDivision(StringFilter sousDivision) {
        this.sousDivision = sousDivision;
    }

    public StringFilter getInfraEmbranchement() {
        return infraEmbranchement;
    }

    public StringFilter infraEmbranchement() {
        if (infraEmbranchement == null) {
            infraEmbranchement = new StringFilter();
        }
        return infraEmbranchement;
    }

    public void setInfraEmbranchement(StringFilter infraEmbranchement) {
        this.infraEmbranchement = infraEmbranchement;
    }

    public StringFilter getMicroEmbranchement() {
        return microEmbranchement;
    }

    public StringFilter microEmbranchement() {
        if (microEmbranchement == null) {
            microEmbranchement = new StringFilter();
        }
        return microEmbranchement;
    }

    public void setMicroEmbranchement(StringFilter microEmbranchement) {
        this.microEmbranchement = microEmbranchement;
    }

    public StringFilter getSuperClasse() {
        return superClasse;
    }

    public StringFilter superClasse() {
        if (superClasse == null) {
            superClasse = new StringFilter();
        }
        return superClasse;
    }

    public void setSuperClasse(StringFilter superClasse) {
        this.superClasse = superClasse;
    }

    public StringFilter getClasse() {
        return classe;
    }

    public StringFilter classe() {
        if (classe == null) {
            classe = new StringFilter();
        }
        return classe;
    }

    public void setClasse(StringFilter classe) {
        this.classe = classe;
    }

    public StringFilter getSousClasse() {
        return sousClasse;
    }

    public StringFilter sousClasse() {
        if (sousClasse == null) {
            sousClasse = new StringFilter();
        }
        return sousClasse;
    }

    public void setSousClasse(StringFilter sousClasse) {
        this.sousClasse = sousClasse;
    }

    public StringFilter getInfraClasse() {
        return infraClasse;
    }

    public StringFilter infraClasse() {
        if (infraClasse == null) {
            infraClasse = new StringFilter();
        }
        return infraClasse;
    }

    public void setInfraClasse(StringFilter infraClasse) {
        this.infraClasse = infraClasse;
    }

    public StringFilter getSuperOrdre() {
        return superOrdre;
    }

    public StringFilter superOrdre() {
        if (superOrdre == null) {
            superOrdre = new StringFilter();
        }
        return superOrdre;
    }

    public void setSuperOrdre(StringFilter superOrdre) {
        this.superOrdre = superOrdre;
    }

    public StringFilter getOrdre() {
        return ordre;
    }

    public StringFilter ordre() {
        if (ordre == null) {
            ordre = new StringFilter();
        }
        return ordre;
    }

    public void setOrdre(StringFilter ordre) {
        this.ordre = ordre;
    }

    public StringFilter getSousOrdre() {
        return sousOrdre;
    }

    public StringFilter sousOrdre() {
        if (sousOrdre == null) {
            sousOrdre = new StringFilter();
        }
        return sousOrdre;
    }

    public void setSousOrdre(StringFilter sousOrdre) {
        this.sousOrdre = sousOrdre;
    }

    public StringFilter getInfraOrdre() {
        return infraOrdre;
    }

    public StringFilter infraOrdre() {
        if (infraOrdre == null) {
            infraOrdre = new StringFilter();
        }
        return infraOrdre;
    }

    public void setInfraOrdre(StringFilter infraOrdre) {
        this.infraOrdre = infraOrdre;
    }

    public StringFilter getMicroOrdre() {
        return microOrdre;
    }

    public StringFilter microOrdre() {
        if (microOrdre == null) {
            microOrdre = new StringFilter();
        }
        return microOrdre;
    }

    public void setMicroOrdre(StringFilter microOrdre) {
        this.microOrdre = microOrdre;
    }

    public StringFilter getSuperFamille() {
        return superFamille;
    }

    public StringFilter superFamille() {
        if (superFamille == null) {
            superFamille = new StringFilter();
        }
        return superFamille;
    }

    public void setSuperFamille(StringFilter superFamille) {
        this.superFamille = superFamille;
    }

    public StringFilter getFamille() {
        return famille;
    }

    public StringFilter famille() {
        if (famille == null) {
            famille = new StringFilter();
        }
        return famille;
    }

    public void setFamille(StringFilter famille) {
        this.famille = famille;
    }

    public StringFilter getSousFamille() {
        return sousFamille;
    }

    public StringFilter sousFamille() {
        if (sousFamille == null) {
            sousFamille = new StringFilter();
        }
        return sousFamille;
    }

    public void setSousFamille(StringFilter sousFamille) {
        this.sousFamille = sousFamille;
    }

    public StringFilter getTribu() {
        return tribu;
    }

    public StringFilter tribu() {
        if (tribu == null) {
            tribu = new StringFilter();
        }
        return tribu;
    }

    public void setTribu(StringFilter tribu) {
        this.tribu = tribu;
    }

    public StringFilter getSousTribu() {
        return sousTribu;
    }

    public StringFilter sousTribu() {
        if (sousTribu == null) {
            sousTribu = new StringFilter();
        }
        return sousTribu;
    }

    public void setSousTribu(StringFilter sousTribu) {
        this.sousTribu = sousTribu;
    }

    public StringFilter getGenre() {
        return genre;
    }

    public StringFilter genre() {
        if (genre == null) {
            genre = new StringFilter();
        }
        return genre;
    }

    public void setGenre(StringFilter genre) {
        this.genre = genre;
    }

    public StringFilter getSousGenre() {
        return sousGenre;
    }

    public StringFilter sousGenre() {
        if (sousGenre == null) {
            sousGenre = new StringFilter();
        }
        return sousGenre;
    }

    public void setSousGenre(StringFilter sousGenre) {
        this.sousGenre = sousGenre;
    }

    public StringFilter getSection() {
        return section;
    }

    public StringFilter section() {
        if (section == null) {
            section = new StringFilter();
        }
        return section;
    }

    public void setSection(StringFilter section) {
        this.section = section;
    }

    public StringFilter getSousSection() {
        return sousSection;
    }

    public StringFilter sousSection() {
        if (sousSection == null) {
            sousSection = new StringFilter();
        }
        return sousSection;
    }

    public void setSousSection(StringFilter sousSection) {
        this.sousSection = sousSection;
    }

    public StringFilter getEspece() {
        return espece;
    }

    public StringFilter espece() {
        if (espece == null) {
            espece = new StringFilter();
        }
        return espece;
    }

    public void setEspece(StringFilter espece) {
        this.espece = espece;
    }

    public StringFilter getSousEspece() {
        return sousEspece;
    }

    public StringFilter sousEspece() {
        if (sousEspece == null) {
            sousEspece = new StringFilter();
        }
        return sousEspece;
    }

    public void setSousEspece(StringFilter sousEspece) {
        this.sousEspece = sousEspece;
    }

    public StringFilter getVariete() {
        return variete;
    }

    public StringFilter variete() {
        if (variete == null) {
            variete = new StringFilter();
        }
        return variete;
    }

    public void setVariete(StringFilter variete) {
        this.variete = variete;
    }

    public StringFilter getSousVariete() {
        return sousVariete;
    }

    public StringFilter sousVariete() {
        if (sousVariete == null) {
            sousVariete = new StringFilter();
        }
        return sousVariete;
    }

    public void setSousVariete(StringFilter sousVariete) {
        this.sousVariete = sousVariete;
    }

    public StringFilter getForme() {
        return forme;
    }

    public StringFilter forme() {
        if (forme == null) {
            forme = new StringFilter();
        }
        return forme;
    }

    public void setForme(StringFilter forme) {
        this.forme = forme;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CronquistPlanteCriteria that = (CronquistPlanteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(superRegne, that.superRegne) &&
            Objects.equals(regne, that.regne) &&
            Objects.equals(sousRegne, that.sousRegne) &&
            Objects.equals(rameau, that.rameau) &&
            Objects.equals(infraRegne, that.infraRegne) &&
            Objects.equals(superDivision, that.superDivision) &&
            Objects.equals(division, that.division) &&
            Objects.equals(sousDivision, that.sousDivision) &&
            Objects.equals(infraEmbranchement, that.infraEmbranchement) &&
            Objects.equals(microEmbranchement, that.microEmbranchement) &&
            Objects.equals(superClasse, that.superClasse) &&
            Objects.equals(classe, that.classe) &&
            Objects.equals(sousClasse, that.sousClasse) &&
            Objects.equals(infraClasse, that.infraClasse) &&
            Objects.equals(superOrdre, that.superOrdre) &&
            Objects.equals(ordre, that.ordre) &&
            Objects.equals(sousOrdre, that.sousOrdre) &&
            Objects.equals(infraOrdre, that.infraOrdre) &&
            Objects.equals(microOrdre, that.microOrdre) &&
            Objects.equals(superFamille, that.superFamille) &&
            Objects.equals(famille, that.famille) &&
            Objects.equals(sousFamille, that.sousFamille) &&
            Objects.equals(tribu, that.tribu) &&
            Objects.equals(sousTribu, that.sousTribu) &&
            Objects.equals(genre, that.genre) &&
            Objects.equals(sousGenre, that.sousGenre) &&
            Objects.equals(section, that.section) &&
            Objects.equals(sousSection, that.sousSection) &&
            Objects.equals(espece, that.espece) &&
            Objects.equals(sousEspece, that.sousEspece) &&
            Objects.equals(variete, that.variete) &&
            Objects.equals(sousVariete, that.sousVariete) &&
            Objects.equals(forme, that.forme) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            superRegne,
            regne,
            sousRegne,
            rameau,
            infraRegne,
            superDivision,
            division,
            sousDivision,
            infraEmbranchement,
            microEmbranchement,
            superClasse,
            classe,
            sousClasse,
            infraClasse,
            superOrdre,
            ordre,
            sousOrdre,
            infraOrdre,
            microOrdre,
            superFamille,
            famille,
            sousFamille,
            tribu,
            sousTribu,
            genre,
            sousGenre,
            section,
            sousSection,
            espece,
            sousEspece,
            variete,
            sousVariete,
            forme,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CronquistPlanteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (superRegne != null ? "superRegne=" + superRegne + ", " : "") +
            (regne != null ? "regne=" + regne + ", " : "") +
            (sousRegne != null ? "sousRegne=" + sousRegne + ", " : "") +
            (rameau != null ? "rameau=" + rameau + ", " : "") +
            (infraRegne != null ? "infraRegne=" + infraRegne + ", " : "") +
            (superDivision != null ? "superDivision=" + superDivision + ", " : "") +
            (division != null ? "division=" + division + ", " : "") +
            (sousDivision != null ? "sousDivision=" + sousDivision + ", " : "") +
            (infraEmbranchement != null ? "infraEmbranchement=" + infraEmbranchement + ", " : "") +
            (microEmbranchement != null ? "microEmbranchement=" + microEmbranchement + ", " : "") +
            (superClasse != null ? "superClasse=" + superClasse + ", " : "") +
            (classe != null ? "classe=" + classe + ", " : "") +
            (sousClasse != null ? "sousClasse=" + sousClasse + ", " : "") +
            (infraClasse != null ? "infraClasse=" + infraClasse + ", " : "") +
            (superOrdre != null ? "superOrdre=" + superOrdre + ", " : "") +
            (ordre != null ? "ordre=" + ordre + ", " : "") +
            (sousOrdre != null ? "sousOrdre=" + sousOrdre + ", " : "") +
            (infraOrdre != null ? "infraOrdre=" + infraOrdre + ", " : "") +
            (microOrdre != null ? "microOrdre=" + microOrdre + ", " : "") +
            (superFamille != null ? "superFamille=" + superFamille + ", " : "") +
            (famille != null ? "famille=" + famille + ", " : "") +
            (sousFamille != null ? "sousFamille=" + sousFamille + ", " : "") +
            (tribu != null ? "tribu=" + tribu + ", " : "") +
            (sousTribu != null ? "sousTribu=" + sousTribu + ", " : "") +
            (genre != null ? "genre=" + genre + ", " : "") +
            (sousGenre != null ? "sousGenre=" + sousGenre + ", " : "") +
            (section != null ? "section=" + section + ", " : "") +
            (sousSection != null ? "sousSection=" + sousSection + ", " : "") +
            (espece != null ? "espece=" + espece + ", " : "") +
            (sousEspece != null ? "sousEspece=" + sousEspece + ", " : "") +
            (variete != null ? "variete=" + variete + ", " : "") +
            (sousVariete != null ? "sousVariete=" + sousVariete + ", " : "") +
            (forme != null ? "forme=" + forme + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

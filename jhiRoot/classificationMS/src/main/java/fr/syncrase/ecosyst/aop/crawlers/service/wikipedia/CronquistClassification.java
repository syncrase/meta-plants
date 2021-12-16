package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers.*;
import fr.syncrase.ecosyst.domain.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

@Component
public class CronquistClassification {

    private final Logger log = LoggerFactory.getLogger(CronquistClassification.class);

    private SuperRegne superRegne;
    private Regne regne;
    private SousRegne sousRegne;
    private Rameau rameau;
    private InfraRegne infraRegne;
    private SuperDivision superDivision;
    private Division division;
    private SousDivision sousDivision;
    private InfraEmbranchement infraEmbranchement;
    private MicroEmbranchement microEmbranchement;
    private SuperClasse superClasse;
    private Classe classe;
    private SousClasse sousClasse;
    private InfraClasse infraClasse;
    private SuperOrdre superOrdre;
    private Ordre ordre;
    private SousOrdre sousOrdre;
    private InfraOrdre infraOrdre;
    private MicroOrdre microOrdre;
    private SuperFamille superFamille;
    private Famille famille;
    private SousFamille sousFamille;
    private Tribu tribu;
    private SousTribu sousTribu;
    private Genre genre;
    private SousGenre sousGenre;
    private Section section;
    private SousSection sousSection;
    private Espece espece;
    private SousEspece sousEspece;
    private Variete variete;
    private SousVariete sousVariete;
    private Forme forme;
    private SousForme sousForme;

    public CronquistClassification() {
        // Descendance
        this.sousForme = new SousForme().nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.forme = new Forme().addSousFormes(sousForme).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousVariete = new SousVariete().addFormes(forme).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.variete = new Variete().addSousVarietes(sousVariete).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousEspece = new SousEspece().addVarietes(variete).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.espece = new Espece().addSousEspeces(sousEspece).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousSection = new SousSection().addEspeces(espece).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.section = new Section().addSousSections(sousSection).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousGenre = new SousGenre().addSections(section).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.genre = new Genre().addSousGenres(sousGenre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousTribu = new SousTribu().addGenres(genre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.tribu = new Tribu().addSousTribus(sousTribu).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousFamille = new SousFamille().addTribus(tribu).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.famille = new Famille().addSousFamilles(sousFamille).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superFamille = new SuperFamille().addFamilles(famille).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.microOrdre = new MicroOrdre().addSuperFamilles(superFamille).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.infraOrdre = new InfraOrdre().addMicroOrdres(microOrdre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousOrdre = new SousOrdre().addInfraOrdres(infraOrdre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.ordre = new Ordre().addSousOrdres(sousOrdre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superOrdre = new SuperOrdre().addOrdres(ordre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.infraClasse = new InfraClasse().addSuperOrdres(superOrdre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousClasse = new SousClasse().addInfraClasses(infraClasse).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.classe = new Classe().addSousClasses(sousClasse).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superClasse = new SuperClasse().addClasses(classe).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.microEmbranchement = new MicroEmbranchement().addSuperClasses(superClasse).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.infraEmbranchement = new InfraEmbranchement().addMicroEmbranchements(microEmbranchement).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousDivision = new SousDivision().addInfraEmbranchements(infraEmbranchement).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.division = new Division().addSousDivisions(sousDivision).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superDivision = new SuperDivision().addDivisions(division).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.infraRegne = new InfraRegne().addSuperDivisions(superDivision).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.rameau = new Rameau().addInfraRegnes(infraRegne).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousRegne = new SousRegne().addRameaus(rameau).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.regne = new Regne().addSousRegnes(sousRegne).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superRegne = new SuperRegne().addRegnes(regne).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);

        // Ascendance
        this.regne.setSuperRegne(superRegne);
        this.sousRegne.setRegne(regne);
        this.rameau.setSousRegne(sousRegne);
        this.infraRegne.setRameau(rameau);
        this.superDivision.setInfraRegne(infraRegne);
        this.division.setSuperDivision(superDivision);
        this.sousDivision.setDivision(division);
        this.infraEmbranchement.setSousDivision(sousDivision);
        this.microEmbranchement.setInfraEmbranchement(infraEmbranchement);
        this.superClasse.setMicroEmbranchement(microEmbranchement);
        this.classe.setSuperClasse(superClasse);
        this.sousClasse.setClasse(classe);
        this.infraClasse.setSousClasse(sousClasse);
        this.superOrdre.setInfraClasse(infraClasse);
        this.ordre.setSuperOrdre(superOrdre);
        this.sousOrdre.setOrdre(ordre);
        this.infraOrdre.setSousOrdre(sousOrdre);
        this.microOrdre.setInfraOrdre(infraOrdre);
        this.superFamille.setMicroOrdre(microOrdre);
        this.famille.setSuperFamille(superFamille);
        this.sousFamille.setFamille(famille);
        this.tribu.setSousFamille(sousFamille);
        this.sousTribu.setTribu(tribu);
        this.genre.setSousTribu(sousTribu);
        this.sousGenre.setGenre(genre);
        this.section.setSousGenre(sousGenre);
        this.sousSection.setSection(section);
        this.espece.setSousSection(sousSection);
        this.sousEspece.setEspece(espece);
        this.variete.setSousEspece(sousEspece);
        this.sousVariete.setVariete(variete);
        this.forme.setSousVariete(sousVariete);
        this.sousForme.setForme(forme);
    }

    public CronquistClassification(@NotNull CronquistRank cronquistRank) {
        CronquistRank currentRank;
        while (cronquistRank.get() != null) {
            currentRank = cronquistRank.get();
            // Make things with the current rank
            currentRank = currentRank.getParent();
        }
        if (cronquistRank.get() instanceof SousForme) {
            this.sousForme = (SousForme) cronquistRank.get();
        }
        // Descendance
        this.forme = new Forme().addSousFormes(sousForme).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousVariete = new SousVariete().addFormes(forme).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.variete = new Variete().addSousVarietes(sousVariete).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousEspece = new SousEspece().addVarietes(variete).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.espece = new Espece().addSousEspeces(sousEspece).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousSection = new SousSection().addEspeces(espece).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.section = new Section().addSousSections(sousSection).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousGenre = new SousGenre().addSections(section).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.genre = new Genre().addSousGenres(sousGenre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousTribu = new SousTribu().addGenres(genre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.tribu = new Tribu().addSousTribus(sousTribu).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousFamille = new SousFamille().addTribus(tribu).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.famille = new Famille().addSousFamilles(sousFamille).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superFamille = new SuperFamille().addFamilles(famille).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.microOrdre = new MicroOrdre().addSuperFamilles(superFamille).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.infraOrdre = new InfraOrdre().addMicroOrdres(microOrdre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousOrdre = new SousOrdre().addInfraOrdres(infraOrdre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.ordre = new Ordre().addSousOrdres(sousOrdre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superOrdre = new SuperOrdre().addOrdres(ordre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.infraClasse = new InfraClasse().addSuperOrdres(superOrdre).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousClasse = new SousClasse().addInfraClasses(infraClasse).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.classe = new Classe().addSousClasses(sousClasse).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superClasse = new SuperClasse().addClasses(classe).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.microEmbranchement = new MicroEmbranchement().addSuperClasses(superClasse).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.infraEmbranchement = new InfraEmbranchement().addMicroEmbranchements(microEmbranchement).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousDivision = new SousDivision().addInfraEmbranchements(infraEmbranchement).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.division = new Division().addSousDivisions(sousDivision).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superDivision = new SuperDivision().addDivisions(division).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.infraRegne = new InfraRegne().addSuperDivisions(superDivision).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.rameau = new Rameau().addInfraRegnes(infraRegne).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.sousRegne = new SousRegne().addRameaus(rameau).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.regne = new Regne().addSousRegnes(sousRegne).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);
        this.superRegne = new SuperRegne().addRegnes(regne).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK);

        // Ascendance
        this.regne.setSuperRegne(superRegne);
        this.sousRegne.setRegne(regne);
        this.rameau.setSousRegne(sousRegne);
        this.infraRegne.setRameau(rameau);
        this.superDivision.setInfraRegne(infraRegne);
        this.division.setSuperDivision(superDivision);
        this.sousDivision.setDivision(division);
        this.infraEmbranchement.setSousDivision(sousDivision);
        this.microEmbranchement.setInfraEmbranchement(infraEmbranchement);
        this.superClasse.setMicroEmbranchement(microEmbranchement);
        this.classe.setSuperClasse(superClasse);
        this.sousClasse.setClasse(classe);
        this.infraClasse.setSousClasse(sousClasse);
        this.superOrdre.setInfraClasse(infraClasse);
        this.ordre.setSuperOrdre(superOrdre);
        this.sousOrdre.setOrdre(ordre);
        this.infraOrdre.setSousOrdre(sousOrdre);
        this.microOrdre.setInfraOrdre(infraOrdre);
        this.superFamille.setMicroOrdre(microOrdre);
        this.famille.setSuperFamille(superFamille);
        this.sousFamille.setFamille(famille);
        this.tribu.setSousFamille(sousFamille);
        this.sousTribu.setTribu(tribu);
        this.genre.setSousTribu(sousTribu);
        this.sousGenre.setGenre(genre);
        this.section.setSousGenre(sousGenre);
        this.sousSection.setSection(section);
        this.espece.setSousSection(sousSection);
        this.sousEspece.setEspece(espece);
        this.variete.setSousEspece(sousEspece);
        this.sousVariete.setVariete(variete);
        this.forme.setSousVariete(sousVariete);
        this.sousForme.setForme(forme);
    }

    public SuperRegne getSuperRegne() {
        return superRegne;
    }

    public Regne getRegne() {
        if (this.getSuperRegne().getRegnes().size() > 1) {
            log.error("Les règnes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.regne;
    }

    public SousRegne getSousRegne() {
        if (this.getRegne().getSousRegnes().size() > 1) {
            log.error("Les sous-règnes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousRegne;
    }

    public Rameau getRameau() {
        if (this.getSousRegne().getRameaus().size() > 1) {
            log.error("Les rameaux sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.rameau;
    }

    public InfraRegne getInfraRegne() {
        if (this.getRameau().getInfraRegnes().size() > 1) {
            log.error("Les infra règnes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.infraRegne;
    }

    public SuperDivision getSuperDivision() {
        if (this.getInfraRegne().getSuperDivisions().size() > 1) {
            log.error("Les super divisions sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.superDivision;
    }

    public Division getDivision() {
        if (this.getSuperDivision().getDivisions().size() > 1) {
            log.error("Les divisions sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.division;
    }

    public SousDivision getSousDivision() {
        if (this.getDivision().getSousDivisions().size() > 1) {
            log.error("Les sous-divisions sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousDivision;
    }

    public InfraEmbranchement getInfraEmbranchement() {
        if (this.getSousDivision().getInfraEmbranchements().size() > 1) {
            log.error("Les infra-embranchements sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.infraEmbranchement;
    }

    public MicroEmbranchement getMicroEmbranchement() {
        if (this.getInfraEmbranchement().getMicroEmbranchements().size() > 1) {
            log.error("Les micro-embranchements sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.microEmbranchement;
    }

    public SuperClasse getSuperClasse() {
        if (this.getMicroEmbranchement().getSuperClasses().size() > 1) {
            log.error("Les super-classes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.superClasse;
    }

    public Classe getClasse() {
        if (this.getSuperClasse().getClasses().size() > 1) {
            log.error("Les classes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.classe;
    }

    public SousClasse getSousClasse() {
        if (this.getClasse().getSousClasses().size() > 1) {
            log.error("Les sous-classes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousClasse;
    }

    public InfraClasse getInfraClasse() {
        if (this.getSousClasse().getInfraClasses().size() > 1) {
            log.error("Les infra-classes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.infraClasse;
    }

    public SuperOrdre getSuperOrdre() {
        if (this.getInfraClasse().getSuperOrdres().size() > 1) {
            log.error("Les super-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.superOrdre;
    }

    public Ordre getOrdre() {
        if (this.getSuperOrdre().getOrdres().size() > 1) {
            log.error("Les super-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.ordre;
    }

    public SousOrdre getSousOrdre() {
        if (this.getOrdre().getSousOrdres().size() > 1) {
            log.error("Les sous-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousOrdre;
    }

    public InfraOrdre getInfraOrdre() {
        if (this.getSousOrdre().getInfraOrdres().size() > 1) {
            log.error("Les infra-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.infraOrdre;
    }

    public MicroOrdre getMicroOrdre() {
        if (this.getInfraOrdre().getMicroOrdres().size() > 1) {
            log.error("Les micro-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.microOrdre;
    }

    public SuperFamille getSuperFamille() {
        if (this.getMicroOrdre().getSuperFamilles().size() > 1) {
            log.error("Les super-familles sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.superFamille;
    }

    public Famille getFamille() {
        if (this.getSuperFamille().getFamilles().size() > 1) {
            log.error("Les super-familles sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.famille;
    }

    public SousFamille getSousFamille() {
        if (this.getFamille().getSousFamilles().size() > 1) {
            log.error("Les sous-familles sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousFamille;
    }

    public Tribu getTribu() {
        if (this.getSousFamille().getTribuses().size() > 1) {
            log.error("Les tribus sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.tribu;
    }

    public SousTribu getSousTribu() {
        if (this.getTribu().getSousTribuses().size() > 1) {
            log.error("Les sous-tribus sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousTribu;
    }

    public Genre getGenre() {
        if (this.getSousTribu().getGenres().size() > 1) {
            log.error("Les sous-tribus sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.genre;
    }

    public SousGenre getSousGenre() {
        if (this.getGenre().getSousGenres().size() > 1) {
            log.error("Les sous-genres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousGenre;
    }

    public Section getSection() {
        if (this.getSousGenre().getSections().size() > 1) {
            log.error("Les sections sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.section;
    }

    public SousSection getSousSection() {
        if (this.getSection().getSousSections().size() > 1) {
            log.error("Les sous-sections sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousSection;
    }

    public Espece getEspece() {
        if (this.getSousSection().getEspeces().size() > 1) {
            log.error("Les espèces sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.espece;
    }

    public SousEspece getSousEspece() {
        if (this.getEspece().getSousEspeces().size() > 1) {
            log.error("Les sous-espèces sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousEspece;
    }

    public Variete getVariete() {
        if (this.getSousEspece().getVarietes().size() > 1) {
            log.error("Les variétés sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.variete;
    }

    public SousVariete getSousVariete() {
        if (this.getVariete().getSousVarietes().size() > 1) {
            log.error("Les sous-variétés sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousVariete;
    }

    public Forme getForme() {
        if (this.getSousVariete().getFormes().size() > 1) {
            log.error("Les sous-variétés sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.forme;
    }

    public SousForme getSousForme() {
        if (this.getForme().getSousFormes().size() > 1) {
            log.error("Les sous-formes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousForme;
    }

    public void clearTail() {
//        if (sousForme.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
//            sousForme=null;
//        } else{
//            return;
//        }
//        for (; sousForme.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK); ) {
//            sousForme=null;
//            return;
//        }
        if (!sousForme.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousForme = null;
        forme.setSousFormes(null);
        if (!forme.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        forme = null;
        sousVariete.setFormes(null);
        if (!sousVariete.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousVariete = null;
        variete.setSousEspece(null);
        if (!variete.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        variete = null;
        sousEspece.setVarietes(null);
        if (!sousEspece.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousEspece = null;
        espece.setSousEspeces(null);
        if (!espece.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        espece = null;
        sousSection.setEspeces(null);
        if (!sousSection.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousSection = null;
        section.setSection(null);
        if (!section.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        section = null;
        sousGenre.setSections(null);
        if (!sousGenre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousGenre = null;
        genre.setSousGenres(null);
        if (!genre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        genre = null;
        sousTribu.setGenres(null);
        if (!sousTribu.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousTribu = null;
        tribu.setSousTribuses(null);
        if (!tribu.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        tribu = null;
        sousFamille.setTribuses(null);
        if (!sousFamille.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousFamille = null;
        famille.setSousFamilles(null);
        if (!famille.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        famille = null;
        superFamille.setFamilles(null);
        if (!superFamille.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        superFamille = null;
        microOrdre.setSuperFamilles(null);
        if (!microOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        microOrdre = null;
        infraOrdre.setMicroOrdres(null);
        if (!infraOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        infraOrdre = null;
        sousOrdre.setInfraOrdres(null);
        if (!sousOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousOrdre = null;
        ordre.setSousOrdres(null);
        if (!ordre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        ordre = null;
        superOrdre.setOrdres(null);
        if (!superOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        superOrdre = null;
        infraClasse.setSuperOrdres(null);
        if (!infraClasse.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        infraClasse = null;
        sousClasse.setInfraClasses(null);
        if (!sousClasse.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousClasse = null;
        classe.setSousClasses(null);
        if (!classe.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        classe = null;
        superClasse.setClasses(null);
        if (!superClasse.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        superClasse = null;
        microEmbranchement.setSuperClasses(null);
        if (!microEmbranchement.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        microEmbranchement = null;
        infraEmbranchement.setMicroEmbranchements(null);
        if (!infraEmbranchement.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        infraEmbranchement = null;
        sousDivision.setInfraEmbranchements(null);
        if (!sousDivision.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousDivision = null;
        division.setSousDivisions(null);
        if (!division.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        division = null;
        superDivision.setDivisions(null);
        if (!superDivision.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        superDivision = null;
        infraRegne.setSuperDivisions(null);
        if (!infraRegne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        infraRegne = null;
        rameau.setInfraRegnes(null);
        if (!rameau.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        rameau = null;
        sousRegne.setRameaus(null);
        if (!sousRegne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        sousRegne = null;
        regne.setSousRegnes(null);
        if (!regne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        regne = null;
        superRegne.setRegnes(null);
        if (!superRegne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
            return;
        }
        superRegne = null;
    }

    /**
     * Construit une liste ascendante des éléments de la classification
     *
     * @param cronquistService contains all autowired services. Each of these are passed to the
     * @return La classification de cronquist complète sous forme de liste. Les éléments null ne sont pas intégré à la liste
     */
    public List<CronquistRank> getAsList(CronquistService cronquistService) {
        List<CronquistRank> list = new ArrayList<>();
        // Attention : l'ordre est important puisque cette liste correspond à la classification
        if (sousForme != null) {
            list.add(new SousFormeWrapper(sousForme, cronquistService.getSousFormeQueryService(), cronquistService.getSousFormeRepository()));
        }
        if (forme != null) {
            list.add(new FormeWrapper(forme, cronquistService.getFormeQueryService(), cronquistService.getFormeRepository()));
        }
        if (sousVariete != null) {
            list.add(new SousVarieteWrapper(sousVariete, cronquistService.getSousVarieteQueryService(), cronquistService.getSousVarieteRepository()));
        }
        if (variete != null) {
            list.add(new VarieteWrapper(variete, cronquistService.getVarieteQueryService(), cronquistService.getVarieteRepository()));
        }
        if (sousEspece != null) {
            list.add(new SousEspeceWrapper(sousEspece, cronquistService.getSousEspeceQueryService(), cronquistService.getSousEspeceRepository()));
        }
        if (espece != null) {
            list.add(new EspeceWrapper(espece, cronquistService.getEspeceQueryService(), cronquistService.getEspeceRepository()));
        }
        if (sousSection != null) {
            list.add(new SousSectionWrapper(sousSection, cronquistService.getSousSectionQueryService(), cronquistService.getSousSectionRepository()));
        }
        if (section != null) {
            list.add(new SectionWrapper(section, cronquistService.getSectionQueryService(), cronquistService.getSectionRepository()));
        }
        if (sousGenre != null) {
            list.add(new SousGenreWrapper(sousGenre, cronquistService.getSousGenreQueryService(), cronquistService.getSousGenreRepository()));
        }
        if (genre != null) {
            list.add(new GenreWrapper(genre, cronquistService.getGenreQueryService(), cronquistService.getGenreRepository()));
        }
        if (sousTribu != null) {
            list.add(new SousTribuWrapper(sousTribu, cronquistService.getSousTribuQueryService(), cronquistService.getSousTribuRepository()));
        }
        if (tribu != null) {
            list.add(new TribuWrapper(tribu, cronquistService.getTribuQueryService(), cronquistService.getTribuRepository()));
        }
        if (sousFamille != null) {
            list.add(new SousFamilleWrapper(sousFamille, cronquistService.getSousFamilleQueryService(), cronquistService.getSousFamilleRepository()));
        }
        if (famille != null) {
            list.add(new FamilleWrapper(famille, cronquistService.getFamilleQueryService(), cronquistService.getFamilleRepository()));
        }
        if (superFamille != null) {
            list.add(new SuperFamilleWrapper(superFamille, cronquistService.getSuperFamilleQueryService(), cronquistService.getSuperFamilleRepository()));
        }
        if (microOrdre != null) {
            list.add(new MicroOrdreWrapper(microOrdre, cronquistService.getMicroOrdreQueryService(), cronquistService.getMicroOrdreRepository()));
        }
        if (infraOrdre != null) {
            list.add(new InfraOrdreWrapper(infraOrdre, cronquistService.getInfraOrdreQueryService(), cronquistService.getInfraOrdreRepository()));
        }
        if (sousOrdre != null) {
            list.add(new SousOrdreWrapper(sousOrdre, cronquistService.getSousOrdreQueryService(), cronquistService.getSousOrdreRepository()));
        }
        if (ordre != null) {
            list.add(new OrdreWrapper(ordre, cronquistService.getOrdreQueryService(), cronquistService.getOrdreRepository()));
        }
        if (superOrdre != null) {
            list.add(new SuperOrdreWrapper(superOrdre, cronquistService.getSuperOrdreQueryService(), cronquistService.getSuperOrdreRepository()));
        }
        if (infraClasse != null) {
            list.add(new InfraClasseWrapper(infraClasse, cronquistService.getInfraClasseQueryService(), cronquistService.getInfraClasseRepository()));
        }
        if (sousClasse != null) {
            list.add(new SousClasseWrapper(sousClasse, cronquistService.getSousClasseQueryService(), cronquistService.getSousClasseRepository()));
        }
        if (classe != null) {
            list.add(new ClasseWrapper(classe, cronquistService.getClasseQueryService(), cronquistService.getClasseRepository()));
        }
        if (superClasse != null) {
            list.add(new SuperClasseWrapper(superClasse, cronquistService.getSuperClasseQueryService(), cronquistService.getSuperClasseRepository()));
        }
        if (microEmbranchement != null) {
            list.add(new MicroEmbranchementWrapper(microEmbranchement, cronquistService.getMicroEmbranchementQueryService(), cronquistService.getMicroEmbranchementRepository()));
        }
        if (infraEmbranchement != null) {
            list.add(new InfraEmbranchementWrapper(infraEmbranchement, cronquistService.getInfraEmbranchementQueryService(), cronquistService.getInfraEmbranchementRepository()));
        }
        if (sousDivision != null) {
            list.add(new SousDivisionWrapper(sousDivision, cronquistService.getSousDivisionQueryService(), cronquistService.getSousDivisionRepository()));
        }
        if (division != null) {
            list.add(new DivisionWrapper(division, cronquistService.getDivisionQueryService(), cronquistService.getDivisionRepository()));
        }
        if (superDivision != null) {
            list.add(new SuperDivisionWrapper(superDivision, cronquistService.getSuperDivisionQueryService(), cronquistService.getSuperDivisionRepository()));
        }
        if (infraRegne != null) {
            list.add(new InfraRegneWrapper(infraRegne, cronquistService.getInfraRegneQueryService(), cronquistService.getInfraRegneRepository()));
        }
        if (rameau != null) {
            list.add(new RameauWrapper(rameau, cronquistService.getRameauQueryService(), cronquistService.getRameauRepository()));
        }
        if (sousRegne != null) {
            list.add(new SousRegneWrapper(sousRegne, cronquistService.getSousRegneQueryService(), cronquistService.getSousRegneRepository()));
        }
        if (regne != null) {
            list.add(new RegneWrapper(regne, cronquistService.getRegneQueryService(), cronquistService.getRegneRepository()));
        }
        if (superRegne != null) {
            list.add(new SuperRegneWrapper(superRegne, cronquistService.getSuperRegneQueryService(), cronquistService.getSuperRegneRepository()));
        }
        return list;
    }
}

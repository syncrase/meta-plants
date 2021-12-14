package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SuperRegneWrapper {

    private final Logger log = LoggerFactory.getLogger(SuperRegneWrapper.class);

    private final SuperRegne superRegne;
    private final Regne regne;
    private final SousRegne sousRegne;
    private final Rameau rameau;
    private final InfraRegne infraRegne;
    private final SuperDivision superDivision;
    private final Division division;
    private final SousDivision sousDivision;
    private final InfraEmbranchement infraEmbranchement;
    private final MicroEmbranchement microEmbranchement;
    private final SuperClasse superClasse;
    private final Classe classe;
    private final SousClasse sousClasse;
    private final InfraClasse infraClasse;
    private final SuperOrdre superOrdre;
    private final Ordre ordre;
    private final SousOrdre sousOrdre;
    private final InfraOrdre infraOrdre;
    private final MicroOrdre microOrdre;
    private final SuperFamille superFamille;
    private final Famille famille;
    private final SousFamille sousFamille;
    private final Tribu tribu;
    private final SousTribu sousTribu;
    private final Genre genre;
    private final SousGenre sousGenre;
    private final Section section;
    private final SousSection sousSection;
    private final Espece espece;
    private final SousEspece sousEspece;
    private final Variete variete;
    private final SousVariete sousVariete;
    private final Forme forme;
    private final SousForme sousForme;

    public SuperRegneWrapper() {
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
}

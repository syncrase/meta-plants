package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class CronquistClassification {

    private final Logger log = LoggerFactory.getLogger(CronquistClassification.class);

    /**
     * Liste de tous les rangs de la classification<br>
     * L'élément 0 est le rang le plus haut : le super règne
     */
    private List<CronquistRank> classificationCronquist;
    private CronquistRank superRegne;
    private CronquistRank regne;
    private CronquistRank sousRegne;
    private CronquistRank rameau;
    private CronquistRank infraRegne;
    private CronquistRank superEmbranchement;
    private CronquistRank embranchement;
    private CronquistRank sousEmbranchement;
    private CronquistRank infraEmbranchement;
    private CronquistRank microEmbranchement;
    private CronquistRank superClasse;
    private CronquistRank classe;
    private CronquistRank sousClasse;
    private CronquistRank infraClasse;
    private CronquistRank superOrdre;
    private CronquistRank ordre;
    private CronquistRank sousOrdre;
    private CronquistRank infraOrdre;
    private CronquistRank microOrdre;
    private CronquistRank superFamille;
    private CronquistRank famille;
    private CronquistRank sousFamille;
    private CronquistRank tribu;
    private CronquistRank sousTribu;
    private CronquistRank genre;
    private CronquistRank sousGenre;
    private CronquistRank section;
    private CronquistRank sousSection;
    private CronquistRank espece;
    private CronquistRank sousEspece;
    private CronquistRank variete;
    private CronquistRank sousVariete;
    private CronquistRank forme;
    private CronquistRank sousForme;

    /**
     * Construit une classification vierge
     */
    public CronquistClassification() {
        initClassification();
    }

    /**
     * Construit une classification à partir d'une arborescence déjà existante
     *
     */
    public CronquistClassification(@NotNull CronquistRank cronquistRank) {

        initClassification();
        // Récupère le premier rang
//        List<CronquistRank> foundElement =
        Optional<CronquistRank> first = classificationCronquist.stream()
            .filter(rank -> rank.getRank().equals(cronquistRank.getRank()))
            .findFirst();

        if (first.isPresent()) {
            // Ajoute tous les parents à l'objet en cours (this)
            int firstExistingRankIndex = classificationCronquist.indexOf(first.get());

            CronquistRank currentRank = cronquistRank;
            do {
                if (!classificationCronquist.get(firstExistingRankIndex).getRank().equals(currentRank.getRank())) {
                    log.error("Tentative d'assigner à un rang les valeurs d'un rang différent");
                }
                classificationCronquist.get(firstExistingRankIndex).setChildren(currentRank.getChildren());
                classificationCronquist.get(firstExistingRankIndex).setParent(currentRank.getParent());
                classificationCronquist.get(firstExistingRankIndex).setId(currentRank.getId());
                classificationCronquist.get(firstExistingRankIndex).setNomFr(currentRank.getNomFr());
                classificationCronquist.get(firstExistingRankIndex).setNomLantin(currentRank.getNomLantin());
                classificationCronquist.get(firstExistingRankIndex).setSynonymes(currentRank.getSynonymes());
                classificationCronquist.get(firstExistingRankIndex).setUrls(currentRank.getUrls());
                firstExistingRankIndex--;
                currentRank = currentRank.getParent();
                if (firstExistingRankIndex < 0 && currentRank != null || firstExistingRankIndex > 0 && currentRank == null) {
                    log.error("Arrivé au bout des rangs à renseigner amis il en reste à traiter. Revoir les index!");
                }
            } while (currentRank != null);
            this.clearTail();
        }


    }

    private void initClassification() {
        constructAscendance();
        constructDescendance();
        constructList();
    }

    private void constructAscendance() {
        this.superRegne = new CronquistRank().rank(CronquistTaxonomikRanks.SUPERREGNE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(null);
        this.regne = new CronquistRank().rank(CronquistTaxonomikRanks.REGNE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(superRegne);
        this.sousRegne = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSREGNE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(regne);
        this.rameau = new CronquistRank().rank(CronquistTaxonomikRanks.RAMEAU).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousRegne);
        this.infraRegne = new CronquistRank().rank(CronquistTaxonomikRanks.INFRAREGNE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(rameau);
        this.superEmbranchement = new CronquistRank().rank(CronquistTaxonomikRanks.SUPEREMBRANCHEMENT).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(infraRegne);
        this.embranchement = new CronquistRank().rank(CronquistTaxonomikRanks.EMBRANCHEMENT).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(superEmbranchement);
        this.sousEmbranchement = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSEMBRANCHEMENT).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(embranchement);
        this.infraEmbranchement = new CronquistRank().rank(CronquistTaxonomikRanks.INFRAEMBRANCHEMENT).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousEmbranchement);
        this.microEmbranchement = new CronquistRank().rank(CronquistTaxonomikRanks.MICROEMBRANCHEMENT).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(infraEmbranchement);
        this.superClasse = new CronquistRank().rank(CronquistTaxonomikRanks.SUPERCLASSE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(microEmbranchement);
        this.classe = new CronquistRank().rank(CronquistTaxonomikRanks.CLASSE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(superClasse);
        this.sousClasse = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSCLASSE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(classe);
        this.infraClasse = new CronquistRank().rank(CronquistTaxonomikRanks.INFRACLASSE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousClasse);
        this.superOrdre = new CronquistRank().rank(CronquistTaxonomikRanks.SUPERORDRE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(infraClasse);
        this.ordre = new CronquistRank().rank(CronquistTaxonomikRanks.ORDRE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(superOrdre);
        this.sousOrdre = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSORDRE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(ordre);
        this.infraOrdre = new CronquistRank().rank(CronquistTaxonomikRanks.INFRAORDRE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousOrdre);
        this.microOrdre = new CronquistRank().rank(CronquistTaxonomikRanks.MICROORDRE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(infraOrdre);
        this.superFamille = new CronquistRank().rank(CronquistTaxonomikRanks.SUPERFAMILLE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(microOrdre);
        this.famille = new CronquistRank().rank(CronquistTaxonomikRanks.FAMILLE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(superFamille);
        this.sousFamille = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSFAMILLE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(famille);
        this.tribu = new CronquistRank().rank(CronquistTaxonomikRanks.TRIBU).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousFamille);
        this.sousTribu = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSTRIBU).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(tribu);
        this.genre = new CronquistRank().rank(CronquistTaxonomikRanks.GENRE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousTribu);
        this.sousGenre = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSGENRE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(genre);
        this.section = new CronquistRank().rank(CronquistTaxonomikRanks.SECTION).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousGenre);
        this.sousSection = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSSECTION).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(section);
        this.espece = new CronquistRank().rank(CronquistTaxonomikRanks.ESPECE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousSection);
        this.sousEspece = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSESPECE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(espece);
        this.variete = new CronquistRank().rank(CronquistTaxonomikRanks.VARIETE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousEspece);
        this.sousVariete = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSVARIETE).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(variete);
        this.forme = new CronquistRank().rank(CronquistTaxonomikRanks.FORME).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(sousVariete);
        this.sousForme = new CronquistRank().rank(CronquistTaxonomikRanks.SOUSFORME).nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK).parent(forme);
    }

    private void constructDescendance() {
        this.superRegne.addChildren(regne);
        this.regne.addChildren(sousRegne);
        this.sousRegne.addChildren(rameau);
        this.rameau.addChildren(infraRegne);
        this.infraRegne.addChildren(superEmbranchement);
        this.superEmbranchement.addChildren(embranchement);
        this.embranchement.addChildren(sousEmbranchement);
        this.sousEmbranchement.addChildren(infraEmbranchement);
        this.infraEmbranchement.addChildren(microEmbranchement);
        this.microEmbranchement.addChildren(superClasse);
        this.superClasse.addChildren(classe);
        this.classe.addChildren(sousClasse);
        this.sousClasse.addChildren(infraClasse);
        this.infraClasse.addChildren(superOrdre);
        this.superOrdre.addChildren(ordre);
        this.ordre.addChildren(sousOrdre);
        this.sousOrdre.addChildren(infraOrdre);
        this.infraOrdre.addChildren(microOrdre);
        this.microOrdre.addChildren(superFamille);
        this.superFamille.addChildren(famille);
        this.famille.addChildren(sousFamille);
        this.sousFamille.addChildren(tribu);
        this.tribu.addChildren(sousTribu);
        this.sousTribu.addChildren(genre);
        this.genre.addChildren(sousGenre);
        this.sousGenre.addChildren(section);
        this.section.addChildren(sousSection);
        this.sousSection.addChildren(espece);
        this.espece.addChildren(sousEspece);
        this.sousEspece.addChildren(variete);
        this.variete.addChildren(sousVariete);
        this.sousVariete.addChildren(forme);
        this.forme.addChildren(sousForme);
        this.sousForme.getChildren().add(null);
//        this.sousForme.addChildren(null);// TODO addChildren also add parent!
    }

    private void constructList() {
        classificationCronquist = new ArrayList<>();
        classificationCronquist.add(this.superRegne);
        classificationCronquist.add(this.regne);
        classificationCronquist.add(this.sousRegne);
        classificationCronquist.add(this.rameau);
        classificationCronquist.add(this.infraRegne);
        classificationCronquist.add(this.superEmbranchement);
        classificationCronquist.add(this.embranchement);
        classificationCronquist.add(this.sousEmbranchement);
        classificationCronquist.add(this.infraEmbranchement);
        classificationCronquist.add(this.microEmbranchement);
        classificationCronquist.add(this.superClasse);
        classificationCronquist.add(this.classe);
        classificationCronquist.add(this.sousClasse);
        classificationCronquist.add(this.infraClasse);
        classificationCronquist.add(this.superOrdre);
        classificationCronquist.add(this.ordre);
        classificationCronquist.add(this.sousOrdre);
        classificationCronquist.add(this.infraOrdre);
        classificationCronquist.add(this.microOrdre);
        classificationCronquist.add(this.superFamille);
        classificationCronquist.add(this.famille);
        classificationCronquist.add(this.sousFamille);
        classificationCronquist.add(this.tribu);
        classificationCronquist.add(this.sousTribu);
        classificationCronquist.add(this.genre);
        classificationCronquist.add(this.sousGenre);
        classificationCronquist.add(this.section);
        classificationCronquist.add(this.sousSection);
        classificationCronquist.add(this.espece);
        classificationCronquist.add(this.sousEspece);
        classificationCronquist.add(this.variete);
        classificationCronquist.add(this.sousVariete);
        classificationCronquist.add(this.forme);
        classificationCronquist.add(this.sousForme);
    }

    public CronquistRank getSuperRegne() {
        return superRegne;
    }

    public CronquistRank getRegne() {
        if (this.getSuperRegne().getChildren().size() > 1) {
            log.error("Les règnes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.regne;
    }

    public CronquistRank getSousRegne() {
        if (this.getRegne().getChildren().size() > 1) {
            log.error("Les sous-règnes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousRegne;
    }

    public CronquistRank getRameau() {
        if (this.getSousRegne().getChildren().size() > 1) {
            log.error("Les rameaux sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.rameau;
    }

    public CronquistRank getInfraRegne() {
        if (this.getRameau().getChildren().size() > 1) {
            log.error("Les infra règnes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.infraRegne;
    }

    public CronquistRank getSuperDivision() {
        if (this.getInfraRegne().getChildren().size() > 1) {
            log.error("Les super divisions sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.superEmbranchement;
    }

    public CronquistRank getEmbranchement() {
        if (this.getSuperDivision().getChildren().size() > 1) {
            log.error("Les divisions sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.embranchement;
    }

    public CronquistRank getSousEmbranchement() {
        if (this.getEmbranchement().getChildren().size() > 1) {
            log.error("Les sous-divisions sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousEmbranchement;
    }

    public CronquistRank getInfraEmbranchement() {
        if (this.getSousEmbranchement().getChildren().size() > 1) {
            log.error("Les infra-embranchements sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.infraEmbranchement;
    }

    public CronquistRank getMicroEmbranchement() {
        if (this.getInfraEmbranchement().getChildren().size() > 1) {
            log.error("Les micro-embranchements sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.microEmbranchement;
    }

    public CronquistRank getSuperClasse() {
        if (this.getMicroEmbranchement().getChildren().size() > 1) {
            log.error("Les super-classes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.superClasse;
    }

    public CronquistRank getClasse() {
        if (this.getSuperClasse().getChildren().size() > 1) {
            log.error("Les classes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.classe;
    }

    public CronquistRank getSousClasse() {
        if (this.getClasse().getChildren().size() > 1) {
            log.error("Les sous-classes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousClasse;
    }

    public CronquistRank getInfraClasse() {
        if (this.getSousClasse().getChildren().size() > 1) {
            log.error("Les infra-classes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.infraClasse;
    }

    public CronquistRank getSuperOrdre() {
        if (this.getInfraClasse().getChildren().size() > 1) {
            log.error("Les super-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.superOrdre;
    }

    public CronquistRank getOrdre() {
        if (this.getSuperOrdre().getChildren().size() > 1) {
            log.error("Les super-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.ordre;
    }

    public CronquistRank getSousOrdre() {
        if (this.getOrdre().getChildren().size() > 1) {
            log.error("Les sous-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousOrdre;
    }

    public CronquistRank getInfraOrdre() {
        if (this.getSousOrdre().getChildren().size() > 1) {
            log.error("Les infra-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.infraOrdre;
    }

    public CronquistRank getMicroOrdre() {
        if (this.getInfraOrdre().getChildren().size() > 1) {
            log.error("Les micro-ordres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.microOrdre;
    }

    public CronquistRank getSuperFamille() {
        if (this.getMicroOrdre().getChildren().size() > 1) {
            log.error("Les super-familles sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.superFamille;
    }

    public CronquistRank getFamille() {
        if (this.getSuperFamille().getChildren().size() > 1) {
            log.error("Les super-familles sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.famille;
    }

    public CronquistRank getSousFamille() {
        if (this.getFamille().getChildren().size() > 1) {
            log.error("Les sous-familles sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousFamille;
    }

    public CronquistRank getTribu() {
        if (this.getSousFamille().getChildren().size() > 1) {
            log.error("Les tribus sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.tribu;
    }

    public CronquistRank getSousTribu() {
        if (this.getTribu().getChildren().size() > 1) {
            log.error("Les sous-tribus sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousTribu;
    }

    public CronquistRank getGenre() {
        if (this.getSousTribu().getChildren().size() > 1) {
            log.error("Les sous-tribus sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.genre;
    }

    public CronquistRank getSousGenre() {
        if (this.getGenre().getChildren().size() > 1) {
            log.error("Les sous-genres sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousGenre;
    }

    public CronquistRank getSection() {
        if (this.getSousGenre().getChildren().size() > 1) {
            log.error("Les sections sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.section;
    }

    public CronquistRank getSousSection() {
        if (this.getSection().getChildren().size() > 1) {
            log.error("Les sous-sections sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousSection;
    }

    public CronquistRank getEspece() {
        if (this.getSousSection().getChildren().size() > 1) {
            log.error("Les espèces sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.espece;
    }

    public CronquistRank getSousEspece() {
        if (this.getEspece().getChildren().size() > 1) {
            log.error("Les sous-espèces sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousEspece;
    }

    public CronquistRank getVariete() {
        if (this.getSousEspece().getChildren().size() > 1) {
            log.error("Les variétés sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.variete;
    }

    public CronquistRank getSousVariete() {
        if (this.getVariete().getChildren().size() > 1) {
            log.error("Les sous-variétés sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousVariete;
    }

    public CronquistRank getForme() {
        if (this.getSousVariete().getChildren().size() > 1) {
            log.error("Les sous-variétés sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.forme;
    }

    public CronquistRank getSousForme() {
        if (this.getForme().getChildren().size() > 1) {
            log.error("Les sous-formes sont trop nombreux. Erreur dans l'algorithme");
        }
        return this.sousForme;
    }

    public void clearTail() {
        for (int i = classificationCronquist.size() - 1; i > 0; i--) {
            if (!Objects.equals(classificationCronquist.get(i).getNomFr(), DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
                break;
            }
            classificationCronquist.remove(i);
            classificationCronquist.get(i - 1).setChildren(null);
        }
    }

    /**
     * Construit une liste descendante des éléments de la classification
     *
     * @return La classification de cronquist complète sous forme de liste. Les éléments null ne sont pas intégré à la liste
     */
    public List<CronquistRank> getList() {
        return classificationCronquist;
    }

    /**
     * Construit une liste descendante des éléments de la classification
     *
     * @return La classification de cronquist complète sous forme de liste. Les éléments null ne sont pas intégré à la liste
     */
    public List<CronquistRank> getReverseList() {
        List<CronquistRank> list = new ArrayList<>();
        for (int i = classificationCronquist.size() - 1; i >= 0; i--) {
            list.add(classificationCronquist.get(i));
        }
        return list;
    }
}

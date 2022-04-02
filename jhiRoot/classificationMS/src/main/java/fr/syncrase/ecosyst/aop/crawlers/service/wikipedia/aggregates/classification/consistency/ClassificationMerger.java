package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.consistency;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.wrapper.CronquistRankWrapper;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.InconsistentRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.MoreThanOneResultException;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.UnknownRankId;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.repository.ClassificationReader;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.IClassificationNom;
import fr.syncrase.ecosyst.domain.ICronquistRank;
import fr.syncrase.ecosyst.domain.IUrl;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.repository.UrlRepository;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.criteria.CronquistRankCriteria;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.LongFilter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassificationMerger {

    private final Logger log = LoggerFactory.getLogger(ClassificationMerger.class);

    private CronquistRankQueryService cronquistRankQueryService;
    private CronquistRankRepository cronquistRankRepository;
    private ClassificationNomRepository classificationNomRepository;
    private UrlRepository urlRepository;
    private ClassificationReader cronquistRankDomainRepository;

    @Autowired
    public void setCronquistRankDomainRepository(ClassificationReader cronquistRankDomainRepository) {
        this.cronquistRankDomainRepository = cronquistRankDomainRepository;
    }

    @Autowired
    public void setCronquistRankQueryService(CronquistRankQueryService cronquistRankQueryService) {
        this.cronquistRankQueryService = cronquistRankQueryService;
    }

    @Autowired
    public void setCronquistRankRepository(CronquistRankRepository cronquistRankRepository) {
        this.cronquistRankRepository = cronquistRankRepository;
    }

    @Autowired
    public void setClassificationNomRepository(ClassificationNomRepository classificationNomRepository) {
        this.classificationNomRepository = classificationNomRepository;
    }

    @Autowired
    public void setUrlRepository(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public ICronquistRank merge(
        @NotNull CronquistClassificationBranch brancheSource,
        @NotNull CronquistClassificationBranch brancheCible,
        @NotNull RankName rankWhereTheMergeIsApplied
                               ) throws UnknownRankId, InconsistentRank, MoreThanOneResultException {

        ICronquistRank rankWithNewNames = copyDataFromRankToRank(
            brancheSource.getRang(rankWhereTheMergeIsApplied),
            brancheCible.getRang(rankWhereTheMergeIsApplied)
                                                                );

        ICronquistRank rankWithAllHisNewTaxons = switchAncestry(
            brancheSource.getRang(rankWhereTheMergeIsApplied),
            rankWithNewNames
                                                               );
        brancheCible.put(rankWhereTheMergeIsApplied, rankWithAllHisNewTaxons);

        supprimeLiaisonAscendante(brancheSource.getRang(rankWhereTheMergeIsApplied));

        return brancheCible.getRang(rankWhereTheMergeIsApplied);
    }

    private void supprimeLiaisonAscendante(@NotNull ICronquistRank rangAPartirDuquelLAscendanceEstSupprimee) {
        Optional<CronquistRank> baseDesRangsASupprimer = cronquistRankRepository.findById(rangAPartirDuquelLAscendanceEstSupprimee.getId());
        baseDesRangsASupprimer.ifPresent(rang -> {
            ICronquistRank wrapper = new CronquistRankWrapper(rang);
            do {
                Set<Long> nomsIds = wrapper.getNoms().stream().map(IClassificationNom::getId).collect(Collectors.toSet());
                log.debug("Suppression des noms " + nomsIds);
                classificationNomRepository.deleteAllById(nomsIds);
                Set<Long> urlsIds = wrapper.getUrls().stream().map(IUrl::getId).collect(Collectors.toSet());
                log.debug("Suppression des urls " + urlsIds);
                urlRepository.deleteAllById(urlsIds);
                log.debug("Suppression du wrapper " + wrapper.getId());
                cronquistRankRepository.deleteById(wrapper.getId());
                wrapper = new CronquistRankWrapper(wrapper.getParent());
            } while (wrapper.isRangDeLiaison());
        });
    }

    /**
     * Associe tous les noms du rangSource au rangCible
     * Associe toutes les urls du rangSource au rangCible
     * Les modifications sont effectives à l'exécution de la méthode
     *
     * @param rangSource Rang qui est dépossédé de ses noms et urls
     * @param rangCible  Rang qui récupère les noms et urls
     * @return Le rang cible avec ses nouveaux noms et urls
     * @throws UnknownRankId    Quand l'un des rangs passés en paramètre ne permet pas de récupérer un rang existant
     * @throws InconsistentRank Quand un rang de liaison possède plusieurs noms
     */
    @Contract("_, _ -> new")
    private @NotNull ICronquistRank copyDataFromRankToRank(
        @NotNull ICronquistRank rangSource,
        @NotNull ICronquistRank rangCible
                                                          ) throws UnknownRankId, InconsistentRank {
        Optional<CronquistRank> rangQuiSeraSupprimeOptional = cronquistRankRepository.findById(rangSource.getId());
        Optional<CronquistRank> rangQuiRecupereLesInfosOptional = cronquistRankRepository.findById(rangCible.getId());
        if (rangQuiSeraSupprimeOptional.isPresent() && rangQuiRecupereLesInfosOptional.isPresent()) {
            ICronquistRank rangQuiSeraSupprime = new CronquistRankWrapper(rangQuiSeraSupprimeOptional.get());
            ICronquistRank rangQuiRecupereLesInfos = new CronquistRankWrapper(rangQuiRecupereLesInfosOptional.get());
            log.debug("Copie les informations du rang " + rangQuiSeraSupprime.getId() + " vers le rang " + rangQuiRecupereLesInfos.getId());

            addNamesFromRankToRank(rangQuiSeraSupprime, rangQuiRecupereLesInfos);
            rangQuiRecupereLesInfos.addAllUrlsToCronquistRank(rangQuiSeraSupprime.getUrls());

            // Enregistrement du rang avec les nouveaux noms
            CronquistRankWrapper rangCibleAInserer = new CronquistRankWrapper(cronquistRankRepository.save(rangQuiRecupereLesInfos.getCronquistRank()));

            // Enregistrement des noms avec le nouveau rang
            rangCibleAInserer.newNames().forEach(classificationNom -> classificationNom.setCronquistRank(rangCibleAInserer.getCronquistRank()));
            classificationNomRepository.saveAll(rangCibleAInserer.newNames());

            // Enregistrement des urls avec le nouveau rang
            rangCibleAInserer.newUrls().forEach(url -> url.setCronquistRank(rangCibleAInserer.getCronquistRank()));
            urlRepository.saveAll(rangCibleAInserer.newUrls());

            // Suppression des noms et des urls de l'ancien rang
            rangQuiSeraSupprime.removeNames();
            rangQuiSeraSupprime.removeUrls();
            cronquistRankRepository.save(rangQuiSeraSupprime.getCronquistRank());
            return new CronquistRankWrapper(rangCibleAInserer.getCronquistRank());
        } else {
            throw new UnknownRankId();
        }

    }

    /**
     * Les taxons du rangSource changent de parents.
     * Les changements sont effectifs après l'exécution de la méthode
     *
     * @param rangSource Rang qui perd tous ses taxons
     * @param rangCible  Rang qui récupère tous les taxons
     * @throws MoreThanOneResultException Si l'un des rangs ne permet pas de récupérer un unique résultat en base de données
     */
    @Contract("_, _ -> new")
    private @NotNull ICronquistRank switchAncestry(ICronquistRank rangSource, ICronquistRank rangCible) throws MoreThanOneResultException {
        // rangCible est déjà synchronisé avec la DB, TODO ne pas fetch de nouveau ?
        ICronquistRank newParent = cronquistRankDomainRepository.queryForCronquistRank(rangCible);
        assert newParent != null;

        // Tous les taxons changent de parent
        // → enregistrement des taxons avec le nouveau parent
        Set<ICronquistRank> taxons = findTaxons(rangSource);
        Set<CronquistRank> taxonsWithNewParent = taxons.stream().map(taxon -> {
            CronquistRank cronquistRank = taxon.getCronquistRank();
            cronquistRank.setParent(newParent.getCronquistRank());
            newParent.getCronquistRank().addChildren(cronquistRank);
            return cronquistRank;
        }).collect(Collectors.toSet());
        cronquistRankRepository.saveAll(taxonsWithNewParent);


        // Enregistrement de l'ancien parent sans ses taxons
        ICronquistRank aSupprimer = cronquistRankDomainRepository.queryForCronquistRank(new AtomicCronquistRank().id(rangSource.getId()));// TODO pas nécessaire de fetch, je l'ai déjà ?
        assert aSupprimer != null;
        aSupprimer.removeTaxons();
        cronquistRankRepository.save(aSupprimer.getCronquistRank());

        return new CronquistRankWrapper(cronquistRankRepository.save(newParent.getCronquistRank()));
    }

    @Contract(pure = true)
    private Set<ICronquistRank> findTaxons(@NotNull ICronquistRank rank) {
        LongFilter rankIdFilter = new LongFilter();
        rankIdFilter.setEquals(rank.getId());

        CronquistRankCriteria rankCrit = new CronquistRankCriteria();
        rankCrit.setId(rankIdFilter);

        return cronquistRankQueryService.findByCriteria(rankCrit).get(0).getChildren().stream().map(CronquistRankWrapper::new).collect(Collectors.toSet());
    }

    private void addNamesFromRankToRank(
        @NotNull ICronquistRank rangSource,
        @NotNull ICronquistRank rangCible
                                       ) throws InconsistentRank {
        if (rangCible.isRangDeLiaison() && rangSource.isRangSignificatif()) {
            if (rangCible.getNoms().size() != 1) {
                throw new InconsistentRank("Le rang de liaison ne doit pas posséder plus d'un nom " + rangCible);
            }
            classificationNomRepository.deleteAllById(
                rangCible.getNoms().stream().map(IClassificationNom::getId).collect(Collectors.toSet())
                                                     );
        }
        rangCible.addAllNamesToCronquistRank(rangSource.getNoms());
    }
}

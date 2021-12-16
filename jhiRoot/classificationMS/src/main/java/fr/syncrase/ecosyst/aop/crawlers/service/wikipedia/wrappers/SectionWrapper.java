package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Section;
import fr.syncrase.ecosyst.repository.SectionRepository;
import fr.syncrase.ecosyst.service.SectionQueryService;
import fr.syncrase.ecosyst.service.criteria.SectionCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SectionWrapper implements CronquistRank {
    private final Section section;
    private final SectionQueryService sectionQueryService;
    private final SectionRepository sectionRepository;

    public SectionWrapper(Section section, SectionQueryService sectionQueryService, SectionRepository sectionRepository) {
        this.section = section;
        this.sectionQueryService = sectionQueryService;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public CronquistRank getParent() {
        return section.getSousGenre();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return section.getSousSections();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (section.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && section.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SectionCriteria sectionCrit = new SectionCriteria();
        sectionCrit.setNomFr(getStringFilterEquals(section.getNomFr()));
        return sectionQueryService.findByCriteria(sectionCrit).stream().map(section1 -> new SectionWrapper(section1, sectionQueryService, sectionRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return section.getId();
    }

    @Override
    public void setId(Long id) {
        section.setId(id);
    }

    @Override
    public String getNomFr() {
        return section.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        section.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return section;
    }

    @Override
    public void save() {
        sectionRepository.save(section);
    }
}

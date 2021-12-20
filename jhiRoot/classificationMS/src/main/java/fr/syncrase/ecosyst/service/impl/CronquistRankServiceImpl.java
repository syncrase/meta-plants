package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.service.CronquistRankService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CronquistRank}.
 */
@Service
@Transactional
public class CronquistRankServiceImpl implements CronquistRankService {

    private final Logger log = LoggerFactory.getLogger(CronquistRankServiceImpl.class);

    private final CronquistRankRepository cronquistRankRepository;

    public CronquistRankServiceImpl(CronquistRankRepository cronquistRankRepository) {
        this.cronquistRankRepository = cronquistRankRepository;
    }

    @Override
    public CronquistRank save(CronquistRank cronquistRank) {
        log.debug("Request to save CronquistRank : {}", cronquistRank);
        return cronquistRankRepository.save(cronquistRank);
    }

    @Override
    public Optional<CronquistRank> partialUpdate(CronquistRank cronquistRank) {
        log.debug("Request to partially update CronquistRank : {}", cronquistRank);

        return cronquistRankRepository
            .findById(cronquistRank.getId())
            .map(existingCronquistRank -> {
                if (cronquistRank.getRank() != null) {
                    existingCronquistRank.setRank(cronquistRank.getRank());
                }
                if (cronquistRank.getNomFr() != null) {
                    existingCronquistRank.setNomFr(cronquistRank.getNomFr());
                }
                if (cronquistRank.getNomLantin() != null) {
                    existingCronquistRank.setNomLantin(cronquistRank.getNomLantin());
                }

                return existingCronquistRank;
            })
            .map(cronquistRankRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CronquistRank> findAll(Pageable pageable) {
        log.debug("Request to get all CronquistRanks");
        return cronquistRankRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CronquistRank> findOne(Long id) {
        log.debug("Request to get CronquistRank : {}", id);
        return cronquistRankRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CronquistRank : {}", id);
        cronquistRankRepository.deleteById(id);
    }
}

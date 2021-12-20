package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.repository.UrlRepository;
import fr.syncrase.ecosyst.service.UrlService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Url}.
 */
@Service
@Transactional
public class UrlServiceImpl implements UrlService {

    private final Logger log = LoggerFactory.getLogger(UrlServiceImpl.class);

    private final UrlRepository urlRepository;

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Url save(Url url) {
        log.debug("Request to save Url : {}", url);
        return urlRepository.save(url);
    }

    @Override
    public Optional<Url> partialUpdate(Url url) {
        log.debug("Request to partially update Url : {}", url);

        return urlRepository
            .findById(url.getId())
            .map(existingUrl -> {
                if (url.getUrl() != null) {
                    existingUrl.setUrl(url.getUrl());
                }

                return existingUrl;
            })
            .map(urlRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Url> findAll(Pageable pageable) {
        log.debug("Request to get all Urls");
        return urlRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Url> findOne(Long id) {
        log.debug("Request to get Url : {}", id);
        return urlRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Url : {}", id);
        urlRepository.deleteById(id);
    }
}

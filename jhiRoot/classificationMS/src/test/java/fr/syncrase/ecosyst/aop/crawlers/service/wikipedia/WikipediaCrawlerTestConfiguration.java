package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.repository.UrlRepository;
import fr.syncrase.ecosyst.service.ClassificationNomQueryService;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.UrlQueryService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

@TestConfiguration
public class WikipediaCrawlerTestConfiguration {

    @Bean
    public CronquistService cronquistService() {
        CronquistService cronquistService = new CronquistService();

        CronquistRankRepository cronquistRankRepository = getCronquistRepository();
        cronquistService.setCronquistRankQueryService(new CronquistRankQueryService(cronquistRankRepository));
        cronquistService.setCronquistRankRepository(cronquistRankRepository);

        UrlRepository urlRepository = getUrlQueryService();
        cronquistService.setUrlQueryService(new UrlQueryService(urlRepository));
        cronquistService.setUrlRepository(urlRepository);

        ClassificationNomRepository classificationNomRepository = getClassificationNomRepository();
        cronquistService.setClassificationNomRepository(classificationNomRepository);
        cronquistService.setClassificationNomQueryService(new ClassificationNomQueryService(classificationNomRepository));

        return cronquistService;
    }

    @Contract(value = " -> new", pure = true)
    private @NotNull ClassificationNomRepository getClassificationNomRepository() {
        return new ClassificationNomRepository() {
            @Override
            public List<ClassificationNom> findAll() {
                return null;
            }

            @Override
            public List<ClassificationNom> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<ClassificationNom> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public <S extends ClassificationNom> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends ClassificationNom> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends ClassificationNom> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<ClassificationNom> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public ClassificationNom getOne(Long aLong) {
                return null;
            }

            @Override
            public ClassificationNom getById(Long aLong) {
                return null;
            }

            @Override
            public <S extends ClassificationNom> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends ClassificationNom> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Optional<ClassificationNom> findOne(Specification<ClassificationNom> spec) {
                return Optional.empty();
            }

            @Override
            public List<ClassificationNom> findAll(Specification<ClassificationNom> spec) {
                return null;
            }

            @Override
            public Page<ClassificationNom> findAll(Specification<ClassificationNom> spec, Pageable pageable) {
                return null;
            }

            @Override
            public List<ClassificationNom> findAll(Specification<ClassificationNom> spec, Sort sort) {
                return null;
            }

            @Override
            public long count(Specification<ClassificationNom> spec) {
                return 0;
            }

            @Override
            public Page<ClassificationNom> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends ClassificationNom> S save(S entity) {
                return null;
            }

            @Override
            public Optional<ClassificationNom> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(ClassificationNom entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends ClassificationNom> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends ClassificationNom> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends ClassificationNom> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends ClassificationNom> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends ClassificationNom> boolean exists(Example<S> example) {
                return false;
            }
        };
    }

    @Contract(value = " -> new", pure = true)
    private @NotNull UrlRepository getUrlQueryService() {
        return new UrlRepository() {
            @Override
            public List<Url> findAll() {
                return null;
            }

            @Override
            public List<Url> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<Url> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public <S extends Url> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Url> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends Url> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<Url> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Url getOne(Long aLong) {
                return null;
            }

            @Override
            public Url getById(Long aLong) {
                return null;
            }

            @Override
            public <S extends Url> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Url> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Optional<Url> findOne(Specification<Url> spec) {
                return Optional.empty();
            }

            @Override
            public List<Url> findAll(Specification<Url> spec) {
                return null;
            }

            @Override
            public Page<Url> findAll(Specification<Url> spec, Pageable pageable) {
                return null;
            }

            @Override
            public List<Url> findAll(Specification<Url> spec, Sort sort) {
                return null;
            }

            @Override
            public long count(Specification<Url> spec) {
                return 0;
            }

            @Override
            public Page<Url> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Url> S save(S entity) {
                return null;
            }

            @Override
            public Optional<Url> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Url entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends Url> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Url> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Url> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Url> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Url> boolean exists(Example<S> example) {
                return false;
            }
        };
    }

    @Contract(value = " -> new", pure = true)
    private @NotNull CronquistRankRepository getCronquistRepository() {
        return new CronquistRankRepository() {
            @Override
            public List<CronquistRank> findAll() {
                return null;
            }

            @Override
            public List<CronquistRank> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<CronquistRank> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public <S extends CronquistRank> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends CronquistRank> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends CronquistRank> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<CronquistRank> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public CronquistRank getOne(Long aLong) {
                return null;
            }

            @Override
            public CronquistRank getById(Long aLong) {
                return null;
            }

            @Override
            public <S extends CronquistRank> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends CronquistRank> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Optional<CronquistRank> findOne(Specification<CronquistRank> spec) {
                return Optional.empty();
            }

            @Override
            public List<CronquistRank> findAll(Specification<CronquistRank> spec) {
                return null;
            }

            @Override
            public Page<CronquistRank> findAll(Specification<CronquistRank> spec, Pageable pageable) {
                return null;
            }

            @Override
            public List<CronquistRank> findAll(Specification<CronquistRank> spec, Sort sort) {
                return null;
            }

            @Override
            public long count(Specification<CronquistRank> spec) {
                return 0;
            }

            @Override
            public Page<CronquistRank> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends CronquistRank> S save(S entity) {
                return null;
            }

            @Override
            public Optional<CronquistRank> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(CronquistRank entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends CronquistRank> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends CronquistRank> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends CronquistRank> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends CronquistRank> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends CronquistRank> boolean exists(Example<S> example) {
                return false;
            }
        };
    }

}

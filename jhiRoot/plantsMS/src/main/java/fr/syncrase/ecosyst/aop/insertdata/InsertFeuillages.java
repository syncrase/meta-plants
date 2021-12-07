package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Feuillage;
import fr.syncrase.ecosyst.repository.FeuillageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.Optional;

public class InsertFeuillages {

    private final Logger log = LoggerFactory.getLogger(InsertStrates.class);
    private FeuillageRepository feuillageRepository;

    public InsertFeuillages(FeuillageRepository feuillageRepository) {
        this.feuillageRepository = feuillageRepository;
    }

    public void insertAllFeuillages() {
        try {
            for (String type : new String[]{"persistant", "semiPersistant", "marcescent", "caduc"}) {
                getOrInsertFeuillage(new Feuillage().type(type));
            }
        } catch (Exception e) {
            log.error("Error when trying to insert in table Feuillage. {" + e.getMessage() + "}");
        }
    }

    private Feuillage getOrInsertFeuillage(Feuillage feuillage) {
        if (!feuillageRepository.exists(Example.of(feuillage))) {
            feuillageRepository.save(feuillage);
        } else {
            Optional<Feuillage> returned = feuillageRepository.findOne(Example.of(feuillage));
            if (returned.isPresent()) {
                feuillage = returned.get();
                log.info("Existing feuillage : " + feuillage);
            } else {
                log.error("Unable to get instance of : " + feuillage);
            }
        }
        return feuillage;
    }
}

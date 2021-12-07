package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Racine;
import fr.syncrase.ecosyst.domain.Strate;
import fr.syncrase.ecosyst.repository.StrateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.Optional;

public class InsertStrates {

    private final Logger log = LoggerFactory.getLogger(InsertStrates.class);
    private StrateRepository strateRepository;

    public InsertStrates(StrateRepository strateRepository) {
        this.strateRepository = strateRepository;
    }

    public void insertAllStrates() {
        try {
            for (String type : new String[]{"hypogee", "muscinale", "herbacee", "arbustive", "arboree"}) {
                getOrInsertStrate(new Strate().type(type));
            }
        } catch (Exception e) {
            log.error("Error when trying to insert in table Racine. {" + e.getMessage() + "}");
        }
    }

    private Strate getOrInsertStrate(Strate strate) {
        if (!strateRepository.exists(Example.of(strate))) {
            strateRepository.save(strate);
        } else {
            Optional<Strate> returned = strateRepository.findOne(Example.of(strate));
            if (returned.isPresent()) {
                strate = returned.get();
                log.info("Existing strate : " + strate);
            } else {
                log.error("Unable to get instance of : " + strate);
            }
        }
        return strate;
    }
}

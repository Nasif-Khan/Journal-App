package com.nasif.jounalApp.cache;

import com.nasif.jounalApp.entity.ConfigJournalApp;
import com.nasif.jounalApp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

//    public enum keys{
//        weather_api,
//        quotes_api
//    }

    public Map<String, String> cache;

    @Autowired
    public ConfigJournalAppRepository configJournalAppRepository;

    @PostConstruct
    public void init(){
        cache = new HashMap<>();
        List<ConfigJournalApp> all = configJournalAppRepository.findAll();
        for (ConfigJournalApp configJournalApp : all) {
            cache.put(configJournalApp.getKey(), configJournalApp.getValue());
        }

    }
}

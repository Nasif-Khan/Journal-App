package com.nasif.jounalApp.scheduler;

import com.nasif.jounalApp.cache.AppCache;
import com.nasif.jounalApp.entity.JournalEntry;
import com.nasif.jounalApp.entity.User;
import com.nasif.jounalApp.enums.Sentiments;
import com.nasif.jounalApp.repository.UserRepositoryImpl;
import com.nasif.jounalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * 0")
//    @Scheduled(cron = "0 * * ? * *")
    public void fetchUsersAndSendEmail() {
        List<User> users = userRepositoryImpl.getUsersForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiments> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiments, Integer> sentimentCounts = new HashMap<>();
            for(Sentiments sentiment : sentiments) {
                if(sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment,0) +1);
                }
            }

            Sentiments mostFrequentSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiments, Integer> entry : sentimentCounts.entrySet()) {
                if(entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if(mostFrequentSentiment != null) {
                emailService.sendEmail(user.getEmail(), "Sentiments for Last 7 days", mostFrequentSentiment.toString());
            }

//            Just to try CRON based sending email (trial). If something fails, un-comment the below
//            lines and comment other lines (business logic) in this loop to make it run without errors
//            String sentiment = sentimentAnalysisService.getSentiment("<Based on the journal entries>");
//            String subject = "Know your Sentiment Score!";
//            emailService.sendEmail(user.getEmail(), subject, sentiment);

            
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAndRefreshCache() {
        appCache.init();
    }
}

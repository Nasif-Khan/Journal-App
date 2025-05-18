package com.nasif.jounalApp.service;

import com.nasif.jounalApp.entity.JournalEntry;
import com.nasif.jounalApp.entity.User;
import com.nasif.jounalApp.repository.JournalEntryRepository;
import com.nasif.jounalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService ;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch(Exception e) {
            log.error("Exception: ", e);
            throw new RuntimeException("An exception occured while saving journal entry", e);
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        try {
            journalEntryRepository.save(journalEntry);
        } catch(Exception e) {
            log.error("Exception: ", e);
        }
    }

    public List<JournalEntry> getJournalEntries() {
        return journalEntryRepository.findAll();
    }

//    public JournalEntry getJournalEntryById(ObjectId id) {
//        return journalEntryRepository.findById(id).orElse(null);
//    }
    public Optional<JournalEntry> getJournalEntryById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteJournalEntryById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e){
            log.error("Exception: ", e);
            throw new RuntimeException("An error occured while deleting journal entry", e);
        }
        return removed;
    }
}

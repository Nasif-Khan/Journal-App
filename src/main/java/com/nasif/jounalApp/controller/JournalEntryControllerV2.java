package com.nasif.jounalApp.controller;

import com.nasif.jounalApp.dto.JournalDTO;
import com.nasif.jounalApp.entity.JournalEntry;
import com.nasif.jounalApp.entity.User;
import com.nasif.jounalApp.mapper.JournalDtoMapper;
import com.nasif.jounalApp.service.JournalEntryService;
import com.nasif.jounalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Tag(name="3. Journal APIs", description = "Get or create journals for logged in user. Update, delete or get journal from the id.")
public class JournalEntryControllerV2 {
    //  controller ---> service ---> repository

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Gets all the journal entries of the current/logged-in user")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all =  user.getJournalEntries();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @Operation(summary = "Creates a new journal entry for the current user")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalDTO currentEntry){
        JournalEntry newEntry = JournalDtoMapper.toEntity(currentEntry);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(newEntry, userName);
            return new ResponseEntity<>(newEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{id}") // myID is the PathVariable
    @Operation(summary = "Get the journal of the current user by its journal id")
    public ResponseEntity<JournalEntry> getJournalByID(@PathVariable String id){
        ObjectId myID = new ObjectId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> userJournals = user.getJournalEntries().stream().filter(journalEntry -> journalEntry.getId().equals(myID)).collect(Collectors.toList());
        if(!userJournals.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(myID);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}

    @DeleteMapping("id/{id}") // myID is the PathVariable
    @Operation(summary = "Deletes the journal of the current user by its journal id")
    public ResponseEntity<Object> deleteJournalByID(@PathVariable String id){
        ObjectId myID = new ObjectId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalEntryService.deleteJournalEntryById(myID, userName);
        if(removed){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{id}")
    @Operation(summary = "Updates the journal of the current user by its journal id")
    public ResponseEntity<JournalEntry> updateJournalByID(@PathVariable String id, @RequestBody JournalDTO currentEntry){
        JournalEntry newEntry = JournalDtoMapper.toEntity(currentEntry);
        ObjectId myID = new ObjectId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> userJournals = user.getJournalEntries().stream().filter(journalEntry -> journalEntry.getId().equals(myID)).collect(Collectors.toList());
        if(!userJournals.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(myID);
            if(journalEntry.isPresent()){
                JournalEntry oldEntry = journalEntry.get();
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setSentiment(newEntry.getSentiment() != null ? newEntry.getSentiment() : oldEntry.getSentiment());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}


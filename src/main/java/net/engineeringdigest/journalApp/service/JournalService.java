package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class JournalService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional // to make it commit or rollback(if any problem occcurs)
    public void saveEntry(JournalEntry newEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            newEntry.setDate(LocalDateTime.now());
            JournalEntry savedEntry = journalEntryRepository.save(newEntry);
            user.getJournalEntryList().add(savedEntry);
            userService.saveEntry(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occured while saving the entry : "+e);
        }
    }

    // method overloaded for update API
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public ArrayList<JournalEntry> getAllEntries() {
        return new ArrayList<>(journalEntryRepository.findAll());
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        user.getJournalEntryList().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }
}

// controller --> service --> repository --> Entity(POJO)
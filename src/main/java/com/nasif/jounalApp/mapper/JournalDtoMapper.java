package com.nasif.jounalApp.mapper;

import com.nasif.jounalApp.dto.JournalDTO;
import com.nasif.jounalApp.entity.JournalEntry;
import com.nasif.jounalApp.entity.User;

public class JournalDtoMapper {
    public static JournalEntry toEntity(JournalDTO dto) {
        JournalEntry entry = new JournalEntry();
        entry.setTitle(dto.getTitle());
        entry.setContent(dto.getContent());
        entry.setSentiment(dto.getSentiment());
        return entry;
    }
}

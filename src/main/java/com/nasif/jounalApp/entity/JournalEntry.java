package com.nasif.jounalApp.entity;

import com.nasif.jounalApp.enums.Sentiments;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal_entries")
@Getter
@Setter
public class JournalEntry {
    @Id
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiments sentiment;

    public String getId(){
        return id != null ? id.toHexString() : null;
    }
}

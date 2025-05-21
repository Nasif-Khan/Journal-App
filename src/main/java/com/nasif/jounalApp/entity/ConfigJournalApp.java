package com.nasif.jounalApp.entity;

import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "config_journal_app")
@Data
public class ConfigJournalApp {
    String key;
    String value;
}

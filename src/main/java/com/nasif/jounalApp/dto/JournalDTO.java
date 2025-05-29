package com.nasif.jounalApp.dto;

import com.nasif.jounalApp.enums.Sentiments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalDTO {
    private String title;
    private String content;
    private Sentiments sentiment;
}

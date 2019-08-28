package hillel.spring.review.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public  class Comment {
    private LocalDateTime timeOfCreation;
    private String comment;
}

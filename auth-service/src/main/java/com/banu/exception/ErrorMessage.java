package com.banu.exception;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class ErrorMessage {

 private int code;

 private String message;

 private List<String> fields;

 @Builder.Default
 private LocalDate date = LocalDate.now();

}

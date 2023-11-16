package com.banu.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

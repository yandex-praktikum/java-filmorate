package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
   private int id;

   @NotBlank
   private  String name;

   @Size(max = 200)
   private  String description;


   private LocalDate releaseDate;


   private Duration duration;

}

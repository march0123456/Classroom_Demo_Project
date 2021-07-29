package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class StudentDTO {
    private Long id;
    private String name;
    private Set<String> courses = new HashSet<>();
}


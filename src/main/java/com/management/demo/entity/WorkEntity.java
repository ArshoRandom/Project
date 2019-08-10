package com.management.demo.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
@Data
@Entity(name = "works")
public class WorkEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    @NotBlank
    String title;
    @Column
    @NotBlank
    double salary;
    @Column
    String Description;


}

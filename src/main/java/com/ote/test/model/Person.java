package com.ote.test.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERSON_INFO")
@Data
public class Person {

 @Id
 @Column(name = "ID")
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer id;
 
 @Column(name = "FIRST_NAME")
 private String firstName;
 
 @Column(name = "LAST_NAME")
 private String lastName;

}
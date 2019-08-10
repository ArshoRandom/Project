package com.management.demo.entity;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "roles")
public enum Role {
    ADMIN,
    USER
}
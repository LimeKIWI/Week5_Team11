package com.example.intermediate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Like_table")
@Builder
public class Like extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Role_Enum role_enum;
    @Column
    private Long Parent_Id;//참조 ID
    @Column
    private Long Member_Id;

}

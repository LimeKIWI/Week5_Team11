package com.example.intermediate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Parent;

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
    private Role_Enum role;
    @Column
    private Long pid;//참조 ID

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}

package com.example.intermediate.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Like{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Role_Enum role_enum;
    //필요한거 참조 아이디 그리고 작성자의 아이니
    @Column(nullable = false)
    private Long Reference_Id;

    @Column(nullable = false)
    private Long Member_Id;





}

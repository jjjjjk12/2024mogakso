package com.ajoudev.practice.repository;

import com.ajoudev.practice.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    void delete(Member member);
    //void update(Member member);
    Optional<Member> findById(String id);
    List<Member> findAll();
}

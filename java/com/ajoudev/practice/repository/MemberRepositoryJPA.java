package com.ajoudev.practice.repository;

import com.ajoudev.practice.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepositoryJPA extends JpaRepository<Member, String>, MemberRepository {
}

package com.ajoudev.practice.repository;

import com.ajoudev.practice.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class MemberRepositoryTmp implements MemberRepository{

    private static Map<String, Member> store = new HashMap<>();
    @Override
    public Member save(Member member) {
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public void delete(Member member) {
        store.remove(member.getId());
    }

    @Override
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}

package com.ajoudev.practice.service;

import com.ajoudev.practice.Member;
import com.ajoudev.practice.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean addMember(Member member) {
        if (!validateDuplicateMember(member)) {
            member.setSalt(getSalt());
            member.setPassword(sha256(member.getPassword(), member.getSalt()));
            memberRepository.save(member);
            return true;
        }

        return false;
    }

    private boolean validateDuplicateMember(Member member) {
        return memberRepository.findById(member.getId()).isPresent();
    }
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    public void editMember(Member origin, Member replace) {
        if (replace.getImage() != null) origin.setImage(replace.getImage());
        origin.setName(replace.getName());
        origin.setSalt(getSalt());
        origin.setPassword(sha256(replace.getPassword(), origin.getSalt()));
        //memberRepository.update(origin);
    }

    public boolean login(Member member) {
        if(memberRepository.findById(member.getId()).isEmpty()) return false;
        Member tmp = memberRepository.findById(member.getId()).get();

        return tmp.getPassword().equals(sha256(member.getPassword(), tmp.getSalt()));
    }

    public Optional<Member> findOne(Member member) {
        return memberRepository.findById(member.getId());
    }
    public Optional<Member> findOne(String id) {
        return memberRepository.findById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    private String getSalt(){

        SecureRandom secureRandom = new SecureRandom();

        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt); // 난수 생성

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < salt.length; i++) {
            sb.append(String.format("%2X", salt[i]));
        }
        return sb.toString();
    }

    private String sha256(String pw, String salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update((pw + salt).getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }
        byte[] pwSalt = md.digest();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < pwSalt.length; i++) {
            sb.append(String.format("%2X", pwSalt[i]));
        }
        return sb.toString();
    }
}

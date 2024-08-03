package com.ajoudev.practice.service;

import com.ajoudev.practice.DTO.UserDetailsDTO;
import com.ajoudev.practice.Member;
import com.ajoudev.practice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findById(username).orElse(null);

        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetailsDTO(member);
    }
}

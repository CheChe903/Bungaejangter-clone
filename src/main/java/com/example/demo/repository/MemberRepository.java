package com.example.demo.repository;

import com.example.demo.domain.Member;

import java.util.HashMap;
import java.util.Map;

public class MemberRepository {
    private final Map<Long, Member> memberMap = new HashMap<>();
    private final Map<String, Long> emailToIdMap = new HashMap<>();

    private long nextId = 1;

    public void addMember(Member member) {
        member.setMemberId(nextId++);
        memberMap.put(member.getMemberId(), member);
        emailToIdMap.put(member.getEmail(), member.getMemberId());

    }
    public Member getMemberById(Long memberId) {
        return memberMap.get(memberId);
    }

    public Member getMemberByEmail(String email) {
        Long memberId = emailToIdMap.get(email);
        return memberId != null ? memberMap.get(memberId) : null;
    }
}

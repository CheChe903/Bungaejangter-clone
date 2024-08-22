package com.example.demo.repository;

import com.example.demo.domain.Member;

import java.util.HashMap;
import java.util.Map;

public class MemberRepository {
    private static MemberRepository instance;
    private final Map<Long, Member> memberMap;
    private final Map<String, Long> emailToIdMap;
    private long nextId;

    private MemberRepository() {
        memberMap = new HashMap<>();
        emailToIdMap = new HashMap<>();
        nextId = 1;
    }

    public static synchronized MemberRepository getInstance() {
        if (instance == null) {
            instance = new MemberRepository();
        }
        return instance;
    }

    public synchronized void addMember(Member member) {
        member.setMemberId(nextId++);
        memberMap.put(member.getMemberId(), member);
        emailToIdMap.put(member.getEmail(), member.getMemberId());
    }

    public synchronized Member getMemberById(Long memberId) {
        return memberMap.get(memberId);
    }

    public synchronized Member getMemberByEmail(String email) {
        Long memberId = emailToIdMap.get(email);
        return memberId != null ? memberMap.get(memberId) : null;
    }
}
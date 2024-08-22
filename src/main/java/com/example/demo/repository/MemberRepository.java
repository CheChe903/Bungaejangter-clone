package com.example.demo.repository;

import com.example.demo.domain.Member;
import com.example.demo.domain.Product;
import com.example.demo.domain.ProductStatus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

    public synchronized List<Product> getProductsListSortedStatus(ProductStatus status) {
        Comparator<Product> statusComparator = Comparator.comparing(
                (Product product) -> product.getStatus() == status ? 0 : 1
        ).thenComparing(Product::getStatus);

        return memberMap.values().stream()
                .flatMap(member -> member.getProducts().stream())
                .sorted(statusComparator)
                .toList();
    }

}
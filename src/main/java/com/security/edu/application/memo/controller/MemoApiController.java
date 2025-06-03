package com.security.edu.application.memo.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/memo")
public class MemoApiController {

    @PersistenceContext
    private EntityManager em;

    @GetMapping
    @Transactional
    public List<?> getMemos() {
        return em.createQuery("SELECT m FROM Memo m").getResultList();
    }

    @PostMapping
    @Transactional
    public String addMemo(@RequestBody Map<String, String> body) {
        String content = body.get("content");
        // ❗ SQL Injection 유도 가능 (예: a'); DROP TABLE memo; -- )
        String unsafeQuery = "INSERT INTO memo (content) VALUES ('" + content + "')";
        em.createNativeQuery(unsafeQuery).executeUpdate();
        return "ok";
    }

    @PostMapping("/delete")
    @Transactional
    public String deleteMemos() {
        em.createQuery("DELETE FROM Memo").executeUpdate();
        return "deleted";
    }
}

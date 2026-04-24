package ru.rogotovsky.deal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rogotovsky.deal.service.DealDocumentsService;

import java.util.UUID;

@RestController
@RequestMapping("/deal/document")
@RequiredArgsConstructor
@Slf4j
public class DealDocumentsController {

    private final DealDocumentsService dealDocumentsService;

    @PostMapping("/{statementId}/send")
    public ResponseEntity<Void> sendDocuments(@PathVariable UUID statementId) {
        log.info("Received /document/{statementId}/send: {}", statementId);
        dealDocumentsService.sendDocuments(statementId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{statementId}/sign")
    public ResponseEntity<Void> signDocuments(@PathVariable UUID statementId, @RequestParam Boolean accepted) {
        log.info("Received /document/{statementId}/sign: {}, accepted={}", statementId, accepted);
        dealDocumentsService.processSigningDecision(statementId, accepted);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{statementId}/code")
    public ResponseEntity<Void> verifySesCode(@PathVariable UUID statementId, @RequestParam String code) {
        log.info("Received /document/{statementId}/code: {}", statementId);
        dealDocumentsService.confirmSesCode(statementId, code);
        return ResponseEntity.noContent().build();
    }
}

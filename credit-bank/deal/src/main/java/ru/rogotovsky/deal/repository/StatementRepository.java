package ru.rogotovsky.deal.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rogotovsky.deal.entity.Statement;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatementRepository extends JpaRepository<Statement, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Statement s where s.statementId = :id")
    Optional<Statement> findByIdForUpdate(UUID id);
}

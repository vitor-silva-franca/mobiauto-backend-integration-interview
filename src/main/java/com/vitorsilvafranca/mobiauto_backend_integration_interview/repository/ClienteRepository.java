package com.vitorsilvafranca.mobiauto_backend_integration_interview.repository;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

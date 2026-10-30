package com.inmobiliaria.repository;

import com.inmobiliaria.model.Inquilino;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InquilinoRepository extends JpaRepository<Inquilino, Long> {
    List<Inquilino> findByContratoActivoTrue();
}

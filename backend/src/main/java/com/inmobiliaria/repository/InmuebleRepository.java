package com.inmobiliaria.repository;

import com.inmobiliaria.model.Inmueble;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InmuebleRepository extends JpaRepository<Inmueble, Long> {}

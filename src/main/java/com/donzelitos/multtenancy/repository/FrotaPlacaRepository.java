package com.donzelitos.multtenancy.repository;

import com.donzelitos.multtenancy.model.FrotaPlaca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrotaPlacaRepository extends JpaRepository<FrotaPlaca, Long> {
}

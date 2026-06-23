package com.catijr.backend.repository;

import com.catijr.backend.model.RegistrationModel;
import com.catijr.backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationModel, Long> {
    Boolean existsByUserIdAndDisciplineId(Long userId, Long disciplineId);
    List<RegistrationModel> findByUserId(Long userId);
    Optional<RegistrationModel> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT SUM(d.creditos) FROM RegistrationModel m JOIN m.discipline d WHERE m.user.id = :userId AND m.status = 'INSCRITO'")
    Integer sumCreditByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(m) > 0 FROM RegistrationModel m JOIN m.discipline d WHERE m.user.id = :userId AND d.codigo = :codigo AND m.status = 'CONCLUIDA'")
    boolean existsByUserIdAndDisciplineCodeAndStatus(@Param("userId") Long userId, @Param("codigo") String codigo);

}

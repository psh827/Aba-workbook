package com.example.backend.repository.treatment;

import com.example.backend.domain.treatmentInfomation.TreatmentInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentInformationRepository extends JpaRepository<TreatmentInformation, Long> {
}

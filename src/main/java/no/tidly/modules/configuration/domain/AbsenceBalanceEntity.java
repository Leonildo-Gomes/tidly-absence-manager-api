package no.tidly.modules.configuration.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import no.tidly.core.shared.BaseEntity;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.domain.EmployeeEntity;

@Entity
@Table(name = "absence_balances")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AbsenceBalanceEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "absence_type_id", nullable = false)
    private AbsenceTypeEntity absenceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "total_entitled", nullable = false, precision = 5, scale = 2)
    private BigDecimal totalEntitled;

    @Column(name = "used_days", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal usedDays = BigDecimal.ZERO;

    @Column(name = "pending_days", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal pendingDays = BigDecimal.ZERO;

    @Column(name = "remaining_days", insertable = false, updatable = false)
    private BigDecimal remainingDays;
}

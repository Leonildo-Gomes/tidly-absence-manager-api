package no.tidly.modules.billing.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import no.tidly.modules.billing.domain.Plan;
import no.tidly.modules.billing.domain.Subscription;
import no.tidly.modules.billing.domain.enums.PlanInterval;
import no.tidly.modules.billing.domain.enums.SubscriptionStatus;
import no.tidly.modules.billing.dto.SubscriptionRequest;
import no.tidly.modules.billing.dto.SubscriptionResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    private final PlanMapper planMapper;

    public Subscription toEntity(SubscriptionRequest request, Plan plan) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = plan.getInterval() == PlanInterval.MONTHLY ? now.plusMonths(1) : now.plusYears(1);

        return Subscription.builder()
                .companyId(request.companyId())
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE) // Default to ACTIVE for now, logic can be more complex
                .currentPeriodStart(now)
                .currentPeriodEnd(endDate)
                .cancelAtPeriodEnd(false)
                .paymentMethodId(request.paymentMethodId())
                .build();
    }

    public SubscriptionResponse toResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getCompanyId(),
                planMapper.toResponse(subscription.getPlan()),
                subscription.getStatus(),
                subscription.getCurrentPeriodStart(),
                subscription.getCurrentPeriodEnd(),
                subscription.getCancelAtPeriodEnd(),
                subscription.getPaymentMethodId());
    }
}

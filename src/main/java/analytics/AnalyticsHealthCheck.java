package analytics;

import com.codahale.metrics.health.HealthCheck;

public class AnalyticsHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}

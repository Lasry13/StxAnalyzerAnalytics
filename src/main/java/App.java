import analytics.*;
import utils.MySqlConnector;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class App extends Application<AnalyticsConfig> {
    @Override
    public void run(AnalyticsConfig configuration,
                    Environment environment) throws Exception {
        environment.healthChecks().register("analytics", new AnalyticsHealthCheck());
        final MySqlConnector mySqlConnector = new MySqlConnector();
        final AnalyticsRepository coronaRepository = new AnalyticsService(mySqlConnector);
        final AnalyticsController analyticsController = new AnalyticsController(coronaRepository);
        environment.jersey().register(analyticsController);
        Cors.insecure(environment);
    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}
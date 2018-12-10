package engine;

import utils.Constant;
import utils.LogGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author lingtang
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
//        generate logs for test use
        LogGenerator logGenerator = new LogGenerator();
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        System.out.println(localDate);
        logGenerator.write(localDate.toString() + ".log", new Random().nextInt(10000) + 10000);
        localDate = localDate.plusDays(1);
        System.out.println(localDate);
        logGenerator.write(localDate.toString() + ".log", new Random().nextInt(10000) + 10000);
        localDate = localDate.plusDays(1);
        System.out.println(localDate);
        logGenerator.write(localDate.toString() + ".log", new Random().nextInt(10000) + 10000);

//        use metrics reporter to get the metrics
        MetricsReporter reporter = new MetricsReporter(Constant.LOG_PATH);
        int p90ReadResponseTime = reporter.getResponseTime("GET", MetricsReporter.Percentile.P90);
        System.out.println("p90 response time is " + p90ReadResponseTime);
        int p95ReadResponseTime = reporter.getResponseTime("GET", MetricsReporter.Percentile.P95);
        System.out.println("p95 response time is " + p95ReadResponseTime);
        int p99ReadResponseTime = reporter.getResponseTime("GET", MetricsReporter.Percentile.P99);
        System.out.println("p99 response time is " + p99ReadResponseTime);
    }
}

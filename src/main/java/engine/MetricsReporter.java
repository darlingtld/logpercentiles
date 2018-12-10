package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetricsReporter {

    enum Percentile {
        P90(90), P95(95), P99(99);

        private int percent;

        Percentile(int percent) {
            this.percent = percent;
        }

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }
    }

    private String logPath;

    public MetricsReporter(String logPath) {
        this.logPath = logPath;
    }

    public int getResponseTime(String httpVerb, Percentile percentile) throws InterruptedException {
        Pattern regex = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\s\\[[0-9T_\\-:.]+]\\s\"" + httpVerb + "\\s[/\\w?=\\d]+\"\\s\\d+\\s(\\d+)");
        File file = new File(logPath);
        File[] logFiles = file.listFiles();
        MetricsStore metricsStore = new MetricsStore();
        if (logFiles != null && logFiles.length > 0) {
            CountDownLatch countDownLatch = new CountDownLatch(logFiles.length);
            ExecutorService executorService = Executors.newFixedThreadPool(logFiles.length);
            for (File logFile : logFiles) {
                executorService.submit(() -> {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(logFile));
                        String line;
                        while ((line = br.readLine()) != null) {
                            Matcher matcher = regex.matcher(line);
                            if (matcher.matches()) {
                                int responseTime = Integer.parseInt(matcher.group(1));
                                metricsStore.put(responseTime);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();

                });
            }
            countDownLatch.await();
            executorService.shutdown();
        }
        return metricsStore.getPercentileValue(percentile);
    }
}

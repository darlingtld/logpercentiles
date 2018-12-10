package engine;

import utils.Constant;

public class MetricsStore {

    private int bucketCount = Constant.MAX_RESPONSE_TIME_IN_MS;
    private int[] bucket = new int[bucketCount];
    private long totalCount = 0;

    public void put(int value) {
        bucket[value]++;
        totalCount++;
    }

    public int getPercentileValue(MetricsReporter.Percentile percentile) {
        int percent = percentile.getPercent();
        int logCount = 0;
        for (int i = bucketCount - 1; i >= 0; i--) {
            if (logCount < (100 - percent) * totalCount / 100) {
                logCount += bucket[i];
            } else {
                return i;
            }
        }
        return 0;
    }
}

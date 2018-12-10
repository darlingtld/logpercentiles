package utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static utils.Constant.LOG_PATH;

/**
 * Randomly generate logs for test use
 *
 * @author lingtang
 */
public class LogGenerator {

    /**
     *
     * @param fileName file which hosts the logs
     * @param lines the amount lines that will be written to the file
     * @throws IOException
     */
    public void write(String fileName, int lines) throws IOException {
        Set<StandardOpenOption> options = new HashSet<>();
        options.add(StandardOpenOption.CREATE);
        options.add(StandardOpenOption.APPEND);
        Path path = Paths.get(LOG_PATH, fileName);
        System.out.println("write to file " + path.toString());
        FileChannel fileChannel = FileChannel.open(path, options);
//        IP_ADDRESS [timestamp] "HTTP_VERB URI" HTTP_ERROR_CODE RESPONSE_TIME_IN_MILLISECONDS
        for (int i = 0; i < lines; i++) {
            String string = fakeLog() + "\r\n";
            byte[] byteArray = string.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
            fileChannel.write(byteBuffer);
        }
        fileChannel.close();
    }

    String fakeLog() {
        return String.format("%s [%s] \"%s\" %s %s", ipAddress(), LocalDateTime.now().toString(), httpVerbAndUri(), httpCode(), responseTimeInMillies());
    }

    private int responseTimeInMillies() {
        return new Random().nextInt(Constant.MAX_RESPONSE_TIME_IN_MS);
    }

    private int httpCode() {
        int r = new Random().nextInt(100);
        if (r < 70) {
            return 200;
        } else if (r < 90) {
            return 500;
        } else {
            return 404;
        }
    }

    private String httpVerbAndUri() {
        int r = new Random().nextInt(100);
        if (r < 20) {
            return String.format("%s %s", "GET", "/api/a?p=" + new Random().nextInt(10));
        } else if (r < 40) {
            return String.format("%s %s", "GET", "/api/b?p=" + new Random().nextInt(10));
        } else if (r < 60) {
            return String.format("%s %s", "GET", "/api/c?p=" + new Random().nextInt(10));
        } else if (r < 80) {
            return String.format("%s %s", "GET", "/api/d?p=" + new Random().nextInt(10));
        } else {
            return String.format("%s %s", "POST", "/api/e");
        }
    }

    private String ipAddress() {
        return String.format("%s.%s.%s.%s", new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
    }


}

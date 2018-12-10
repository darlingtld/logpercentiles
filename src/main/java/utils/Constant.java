package utils;

import java.nio.file.Paths;

public interface Constant {
    String LOG_PATH = Paths.get("src", "main", "resources").toString();

    Integer MAX_RESPONSE_TIME_IN_MS = 100_000;
}

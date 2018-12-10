package utils;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;

public class LogGeneratorTest {

    private LogGenerator logGenerator = new LogGenerator();

    @Test
    public void testGenerateLogs() throws IOException {
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        System.out.println(localDate);
        logGenerator.write(localDate.toString() + ".log", new Random().nextInt(10000) + 10000);
        localDate = localDate.plusDays(1);
        System.out.println(localDate);
        logGenerator.write(localDate.toString() + ".log", new Random().nextInt(10000) + 10000);
        localDate = localDate.plusDays(1);
        System.out.println(localDate);
        logGenerator.write(localDate.toString() + ".log", new Random().nextInt(10000) + 10000);
    }

    @Test
    public void testFakeLog() {
        String log = logGenerator.fakeLog();
        System.out.println(log);
        assertThat("log should match pattern", log.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\s\\[[0-9T_\\-:.]+]\\s\"(GET|POST)\\s[/\\w?=\\d]+\"\\s\\d+\\s\\d+"));
    }

    @Test
    public void testResponseTimeExtraction(){
        String log = logGenerator.fakeLog();
        System.out.println(log);
        String httpVerb = "GET";
        Pattern regex = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\s\\[[0-9T_\\-:.]+]\\s\"" + httpVerb + "\\s[/\\w?=\\d]+\"\\s\\d+\\s(\\d+)");
        Matcher matcher = regex.matcher(log);
        if(matcher.matches()){
            System.out.println(matcher.group(1));
        }

    }
}

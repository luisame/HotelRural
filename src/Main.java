
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Info log message");
        logger.debug("Debug log message");
        logger.error("Error log message");
    }
}

package unibo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class LedUpdatesFilter extends RunnableFilter {

    private static final Logger logger = LoggerFactory.getLogger(LedUpdatesFilter.class);
    private final String messagePrefix;

    public LedUpdatesFilter(BufferedInputStream in, BufferedOutputStream out, String messagePrefix) {
        super(in, out);
        this.messagePrefix = messagePrefix;
    }

    @Override
    public boolean filter(String message) {
        logger.debug("filter | " + message);
        return !message.contains(messagePrefix);
    }
}

package ru.study;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.study.Constants.REGEX_MAX_LENGTH;

public class RegexMatchesTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegexMatchesTask.class);

    private final InterruptibleCharSequence text;

    private final String regex;

    private RegexMatchesTask(@NotNull String text, @NotNull String regex) {
        if (regex.length() > REGEX_MAX_LENGTH) {
            throw new IllegalArgumentException(Constants.REGEX_TOO_LARGE_MESSAGE);
        }
        this.text = new InterruptibleCharSequence(text);
        this.regex = regex;
    }

    @Override
    public Boolean call() {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher  = pattern.matcher(text);
        return matcher.matches();
    }

    static class RegexMatchesTaskFactory {
        public static Optional<RegexMatchesTask> build(@NotNull String text, @NotNull String regex) {
            try {
                return Optional.of(new RegexMatchesTask(text, regex));
            } catch (IllegalArgumentException e) {
                LOGGER.warn(e.getMessage());
                return Optional.empty();
            }
        }
    }
}
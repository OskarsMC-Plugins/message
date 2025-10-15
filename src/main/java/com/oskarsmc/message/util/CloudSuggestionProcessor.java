package com.oskarsmc.message.util;

import com.velocitypowered.api.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProcessor;

import java.util.Locale;
import java.util.stream.Stream;

/**
 * Lowercase Cloud Suggestion Processor
 */
public final class CloudSuggestionProcessor implements SuggestionProcessor<CommandSource> {

  @Override
  public @NonNull Stream<@NonNull Suggestion> process(@NonNull CommandPreprocessingContext<CommandSource> context, @NonNull Stream<@NonNull Suggestion> suggestions) {
    final String currentInput = context.commandInput().isEmpty()
        ? ""
        : context.commandInput().peekString().toLowerCase(Locale.ROOT);

    return suggestions
        .filter(suggestion -> suggestion.suggestion().toLowerCase(Locale.ROOT).startsWith(currentInput));
  }
}

package com.oskarsmc.message.locale;

import com.google.gson.Gson;
import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * The translation manager in charge of registering translations.
 */
public final class TranslationManager {
    /**
     * Construct the translation manager.
     * @param logger Logger
     */
    @Inject
    public TranslationManager(@NotNull Logger logger) {
        logger.info("Loading Translations...");
        TranslationRegistry translationRegistry = TranslationRegistry.create(Key.key("oskarsmc", "message"));
        translationRegistry.defaultLocale(Locale.ENGLISH);

        TranslationFile translationFile = readTranslationFile();
        if (translationFile != null) {
            for (TranslationFile.LocaleTranslation localeTranslation : translationFile.translations()) {
                Locale locale = Locale.forLanguageTag(localeTranslation.languageTag());
                for (TranslationFile.LocaleTranslation.Translation translation : localeTranslation.translations())
                    translationRegistry.register(translation.key(), locale, new MessageFormat(translation.value().replace("'", "''")));
            }
            logger.info("Loaded {} translations.", translationFile.translations().size());
        } else {
            logger.error("Could not find a translations file. Continuing without translations.");
        }

        GlobalTranslator.get().addSource(translationRegistry);
    }

    /**
     * Reads the translation file and maps it to an {@link TranslationFile} instance.
     * @return The mapped {@link TranslationFile} instance.
     */
    public static @Nullable TranslationFile readTranslationFile() {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(TranslationManager.class.getResourceAsStream("/translations.json")))) {
            return new Gson().fromJson(reader, TranslationFile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

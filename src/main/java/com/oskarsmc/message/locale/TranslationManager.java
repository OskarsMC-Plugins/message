package com.oskarsmc.message.locale;

import com.google.gson.Gson;
import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;

public final class TranslationManager {
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
            logger.info("Loaded " + translationFile.translations().size() + " translations.");
        } else {
            logger.severe("Could not find a translations file. Continuing without translations.");
        }

        GlobalTranslator.get().addSource(translationRegistry);
    }

    public static @Nullable TranslationFile readTranslationFile() {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(TranslationManager.class.getResourceAsStream("/translations.json")))) {
            return new Gson().fromJson(reader, TranslationFile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

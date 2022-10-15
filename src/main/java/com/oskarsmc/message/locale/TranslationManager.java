package com.oskarsmc.message.locale;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The translation manager in charge of registering translations.
 */
public final class TranslationManager {
    @Inject
    private Logger logger;

    /**
     * Construct the translation manager.
     */
    @Inject
    public TranslationManager(@NotNull Logger logger, @DataDirectory @NotNull Path dataDirectory) {
        this.logger = logger;
        logger.info("Loading Translations...");
        TranslationRegistry translationRegistry = TranslationRegistry.create(Key.key("oskarsmc", "message"));
        translationRegistry.defaultLocale(Locale.ENGLISH);

        TranslationFile translationFile = readTranslationFile(dataDirectory.resolve("translations/"));
        if (translationFile != null) {
            for (TranslationFile.LocaleTranslation localeTranslation : translationFile.translations()) {
                Locale locale = Locale.forLanguageTag(localeTranslation.languageTag());
                for (Map.Entry<String, String> entry : localeTranslation.translations().entrySet()) {
                    translationRegistry.register(entry.getKey(), locale, new MessageFormat(entry.getValue().replace("'", "''")));
                }
            }
            logger.info("Loaded {} translations.", translationFile.translations().size());
        } else {
            logger.error("Could not find a translations file. Continuing without translations.");
        }

        GlobalTranslator.translator().addSource(translationRegistry);
    }

    /**
     * Reads the translation file and maps it to an {@link TranslationFile} instance.
     *
     * @return The mapped {@link TranslationFile} instance.
     */
    public @Nullable TranslationFile readTranslationFile(@NotNull Path translationsDirectory) {
        Gson gson = new Gson();
        String version = TranslationManager.class.getPackage().getImplementationVersion();
        Path currentTranslationFile = translationsDirectory.resolve("translations-" + version + ".json");
        Path relativeCurrentTranslationFile = Path.of(".").relativize(currentTranslationFile);

        try {
            if (!Files.exists(translationsDirectory)) Files.createDirectory(translationsDirectory);

            if (Files.exists(currentTranslationFile)) {
                TranslationFile definedTranslationFile = gson.fromJson(Files.readString(currentTranslationFile), TranslationFile.class);
                if (!Objects.equals(definedTranslationFile.translationVersion(), version)) {
                    logger.warn("The custom translation file located at {} is outdated (expected {}, got {}).",
                            relativeCurrentTranslationFile, version, definedTranslationFile.translationVersion());
                }
                return definedTranslationFile;
            } else {
                Files.copy(Objects.requireNonNull(getClass().getResourceAsStream("/translations.json")), currentTranslationFile);
                logger.info("Creating translation file {}...", relativeCurrentTranslationFile);
                return readTranslationFile(translationsDirectory);
            }
        } catch (IOException | JsonParseException exception) {
            exception.printStackTrace();
            logger.error("Could not read, parse, or write to {}", relativeCurrentTranslationFile);
            return null;
        }
    }
}

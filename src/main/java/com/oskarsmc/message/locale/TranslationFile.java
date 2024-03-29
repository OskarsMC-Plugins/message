package com.oskarsmc.message.locale;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * A Translation file.
 */
public final class TranslationFile {
    @SerializedName("translation-version")
    private String translationVersion;

    @SerializedName("translations")
    private List<LocaleTranslation> translations;

    /**
     * Get the version of the translation file.
     *
     * @return The version of the translation file.
     */
    public String translationVersion() {
        return translationVersion;
    }

    /**
     * All the translations in the translation file.
     *
     * @return The translations in the translation file.
     */
    public List<LocaleTranslation> translations() {
        return translations;
    }

    /**
     * Translations for a language.
     */
    public final static class LocaleTranslation {
        @SerializedName("language-tag")
        private String languageTag;

        @SerializedName("translations")
        private Map<String, String> translations;

        /**
         * Get the language tag of the translation.
         *
         * @return The language tag of the translation.
         */
        public String languageTag() {
            return languageTag;
        }

        /**
         * Get all translations in the {@link LocaleTranslation}.
         *
         * @return All translations in the {@link LocaleTranslation}.
         */
        public Map<String, String> translations() {
            return translations;
        }
    }
}

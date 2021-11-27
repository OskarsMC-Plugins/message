package com.oskarsmc.message.locale;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class TranslationFile {
    @SerializedName("translation-version")
    private String translationVersion;

    @SerializedName("translations")
    private List<LocaleTranslation> translations;

    public String translationVersion() {
        return translationVersion;
    }

    public List<LocaleTranslation> translations() {
        return translations;
    }

    public final static class LocaleTranslation {
        @SerializedName("language-tag")
        private String languageTag;

        @SerializedName("translations")
        private List<Translation> translations;

        public String languageTag() {
            return languageTag;
        }

        public List<Translation> translations() {
            return translations;
        }


        public final static class Translation {
            @SerializedName("key")
            private String key;

            @SerializedName("value")
            private String value;

            public String key() {
                return key;
            }

            public String value() {
                return value;
            }
        }
    }
}

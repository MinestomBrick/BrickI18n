package org.minestombrick.i18n.tests;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;
import org.minestombrick.i18n.api.translation.I18nNamespaceRegistry;
import org.minestombrick.i18n.api.translation.namespace.I18nNamespace;
import org.minestombrick.i18n.app.manager.DefaultINamespaceRegistry;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class I18nTests {

    @Test
    public void translationNamespaceTest() {
        I18nNamespace global = new I18nNamespace("global", Locale.ENGLISH);
        global.loadValues(getClass().getClassLoader().getResource("languages/en.json"));

        Component translated = global.translate(Locale.ENGLISH, Component.translatable("test.message")
                .args(Component.text("there")));

        String result = PlainTextComponentSerializer.plainText().serialize(translated);
        assertEquals("Hey there!", result);
    }

}

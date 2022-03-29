package org.minestombrick.i18n.app.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.minestombrick.i18n.api.namespace.I18nNamespace;

import java.util.Locale;

public class GlobalNamespace extends I18nNamespace {

    public GlobalNamespace(String id, Locale defaultLocale) {
        super(id, defaultLocale);
    }

    @Override
    public Component translate(Locale locale, TranslatableComponent component) {
        return renderer.render(component, locale);
    }


}

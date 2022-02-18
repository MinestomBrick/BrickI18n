package org.minestombrick.i18n.api.translation;

import org.jetbrains.annotations.NotNull;
import org.minestombrick.i18n.api.translation.namespace.I18nNamespace;

public interface I18nNamespaceRegistry {

    void register(@NotNull I18nNamespace namespace);

    I18nNamespace byId(@NotNull String namespace);

    I18nNamespace byObject(@NotNull Object object);

}

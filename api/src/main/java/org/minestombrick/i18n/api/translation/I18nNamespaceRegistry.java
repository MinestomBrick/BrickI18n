package org.minestombrick.i18n.api.translation;

import org.minestombrick.i18n.api.translation.namespace.I18nNamespace;

public interface I18nNamespaceRegistry {

    void register(I18nNamespace namespace);

    I18nNamespace byId(String namespace);

}

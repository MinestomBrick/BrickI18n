package org.minestombrick.i18n.app.manager;

import org.minestombrick.i18n.api.translation.I18nNamespaceRegistry;
import org.minestombrick.i18n.api.translation.namespace.I18nNamespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultINamespaceRegistry implements I18nNamespaceRegistry {

    private final Logger LOGGER = LoggerFactory.getLogger(DefaultINamespaceRegistry.class);

    private final Map<String, I18nNamespace> namespaces = new HashMap<>();

    public DefaultINamespaceRegistry() {
        register(new I18nNamespace("global", Locale.ENGLISH));
    }

    @Override
    public void register(I18nNamespace namespace) {
        namespaces.put(namespace.id(), namespace);
    }

    @Override
    public I18nNamespace byId(String namespace) {
        return namespaces.get(namespace);
    }

}

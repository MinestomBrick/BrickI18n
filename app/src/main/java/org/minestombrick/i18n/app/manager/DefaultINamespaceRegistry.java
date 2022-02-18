package org.minestombrick.i18n.app.manager;

import net.minestom.server.extensions.ExtensionClassLoader;
import org.jetbrains.annotations.NotNull;
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
    public void register(@NotNull I18nNamespace namespace) {
        namespaces.put(namespace.id(), namespace);
    }

    @Override
    public I18nNamespace byId(@NotNull String namespace) {
        if ( namespaces.containsKey(namespace) ) {
            return namespaces.get(namespace);
        }
        return namespaces.get("global"); // default
    }

    @Override
    public I18nNamespace byObject(@NotNull Object object) {
        ClassLoader classLoader = object.getClass().getClassLoader();
        if ( classLoader instanceof ExtensionClassLoader ecl ) {
            return byId(ecl.getName().substring(4));
        }
        return namespaces.get("global"); // default
    }

}

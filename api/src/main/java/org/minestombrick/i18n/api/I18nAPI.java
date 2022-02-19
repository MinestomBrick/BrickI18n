package org.minestombrick.i18n.api;

import org.minestombrick.i18n.api.namespace.I18nNamespace;

public class I18nAPI {

    private static I18nNamespaceRegistry namespaceRegistry;

    public static void setNamespaceRegistry(I18nNamespaceRegistry registry) {
        namespaceRegistry = registry;
    }

    //

    public static I18nNamespaceRegistry get() {
        return namespaceRegistry;
    }

    public static I18nNamespace get(String namespace) {
        return get().byId(namespace);
    }

    public static I18nNamespace get(Object object) {
        return get().byObject(object);
    }

}

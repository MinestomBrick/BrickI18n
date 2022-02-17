package org.minestombrick.i18n.api.translation;

public class I18nAPI {

    private static I18nNamespaceRegistry namespaceRegistry;

    public static void setNamespaceRegistry(I18nNamespaceRegistry registry) {
        namespaceRegistry = registry;
    }

    public static I18nNamespaceRegistry get() {
        return namespaceRegistry;
    }

}

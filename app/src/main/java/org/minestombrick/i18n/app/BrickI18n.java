package org.minestombrick.i18n.app;

import net.minestom.server.extensions.Extension;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.i18n.api.I18nNamespaceRegistry;
import org.minestombrick.i18n.api.namespace.I18nNamespace;
import org.minestombrick.i18n.app.manager.DefaultINamespaceRegistry;
import org.minestombrick.i18n.app.manager.GlobalNamespace;

import java.util.Locale;

public class BrickI18n extends Extension {

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        I18nNamespaceRegistry registry = new DefaultINamespaceRegistry();
        I18nAPI.setNamespaceRegistry(registry);

        // global namespace
        I18nNamespace global = new GlobalNamespace("global", Locale.ENGLISH);
        registry.register(global);

        // load global default values
        global.loadValues(this, "languages");

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getOrigin().getName() + " v" + getOrigin().getVersion();
    }

}

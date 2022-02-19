package org.minestombrick.i18n.app;

import net.minestom.server.extensions.Extension;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.i18n.api.I18nNamespaceRegistry;
import org.minestombrick.i18n.app.manager.DefaultINamespaceRegistry;

public class BrickI18n extends Extension {

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        I18nNamespaceRegistry registry = new DefaultINamespaceRegistry();
        I18nAPI.setNamespaceRegistry(registry);

        // global namespace
        registry.byId("global").loadValues(this, "languages");

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

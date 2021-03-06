package org.minestombrick.i18n.api.namespace;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.TranslationRegistry;
import net.minestom.server.adventure.Localizable;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.extensions.Extension;
import org.minestombrick.i18n.api.I18nAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class I18nNamespace {

    private final Logger LOGGER = LoggerFactory.getLogger(I18nNamespace.class);

    protected final TranslationRegistry registry;
    protected final Locale defaultLocale;
    protected final String id;

    protected final ComponentRenderer renderer;

    public I18nNamespace(String id, Locale defaultLocale) {
        final Key key = Key.key("i18n:" + id.toLowerCase());
        this.registry = TranslationRegistry.create(key);
        this.renderer = ComponentRenderer.usingTranslationSource(registry);
        this.defaultLocale = defaultLocale;
        this.id = id;

        this.registry.defaultLocale(defaultLocale);
    }

    public I18nNamespace(Extension extension, Locale defaulLocale) {
        this(extension.getOrigin().getName(), defaulLocale);
    }

    // api

    public final String id() {
        return id;
    }

    public Component translate(Locale locale, TranslatableComponent component) {
        if (registry.contains(component.key()) ) {
            return renderer.render(component, locale);
        } else {
            return I18nAPI.global().translate(locale, component);
        }
    }

    public final Component translate(Localizable localizable, TranslatableComponent component) {
        Locale locale = localizable.getLocale();
        if (locale == null) locale = defaultLocale;
        return translate(locale, component);
    }

    public final Component translate(Localizable localizable, String key) {
        return translate(localizable, Component.translatable(key));
    }

    public final Component translate(Localizable localizable, String key, Object... args) {
        return translate(localizable, translatable(key, args));
    }

    public final Component translate(Localizable localizable, String key, Component... args) {
        return translate(localizable, translatable(key, args));
    }

    public final void send(CommandSender sender, TranslatableComponent component) {
        if (sender instanceof Player p) {
            sender.sendMessage(translate(p, component));
            return;
        }
        sender.sendMessage(translate(defaultLocale, component));
    }

    public final void send(CommandSender sender, String key) {
        send(sender, Component.translatable(key));
    }

    public final void send(CommandSender sender, String key, Object... args) {
        send(sender, translatable(key, args));
    }

    public final void send(CommandSender sender, String key, Component... args) {
        send(sender, translatable(key, args));
    }

    private TranslatableComponent translatable(String key, Object... args) {
        Component[] cargs = new Component[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                cargs[i] = Component.text("null");
            } else if (args[i] instanceof Component comp) {
                cargs[i] = comp;
            } else {
                cargs[i] = Component.text(args[i].toString());
            }
        }

        return Component.translatable(key).args(cargs);
    }

    private TranslatableComponent translatable(String key, Component... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                args[i] = Component.text("null");
            }
        }

        return Component.translatable(key).args(args);
    }

    // LOADING

    public final void loadValues(Extension extension, String pathToResources) {
        // load files from data directory (highest priority)
        File directory = extension.getDataDirectory().resolve(pathToResources).toFile();
        if (directory.exists() || directory.mkdirs()) {
            for (File file : directory.listFiles()) {
                try {
                    loadValues(file.toPath());
                } catch (IOException ex) {
                    throw new RuntimeException("Cannot read resource '" + file.getName() + "'", ex);
                }
            }
        }

        // look for embeded files
        URL url = extension.getOrigin().getClassLoader().getResource(pathToResources);
        if (url == null) {
            throw new RuntimeException("Resource not found.");
        }

        URI uri;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid resource path.", e);
        }

        // load embedded files from jar (lowest priority)
        try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            Path root = fs.getPath(pathToResources);
            Stream<Path> pathStream = Files.walk(root);
            for (Iterator<Path> it = pathStream.iterator(); it.hasNext(); ) {
                Path path = it.next();
                if (!path.getFileName().toString().contains(".")) {
                    continue;
                }


                Path targetFile = extension.getDataDirectory().resolve(pathToResources).resolve(root.relativize(path).toString());
                if (!targetFile.toFile().exists()) {
                    try (InputStream is = path.toUri().toURL().openStream()) {
                        Files.createDirectories(targetFile.getParent());
                        Files.copy(is, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        LOGGER.warn("Cannot save packaged resource '" + path + "' of extension '" + extension.getOrigin().getName() + "'.");
                    }
                    continue;
                }

                loadValues(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot traverse files of given path.", e);
        }
    }

    public final void loadValues(Path path) throws MalformedURLException {
        Locale locale = Locale.forLanguageTag(path.getFileName().toString().split("\\.")[0]);
        loadValues(path.toUri().toURL(), locale);
    }

    public final void loadValues(URL resource) {
        String[] parts = resource.getFile().split("/");
        Locale locale = Locale.forLanguageTag(parts[parts.length - 1].split("\\.")[0]);
        loadValues(resource, locale);
    }

    public final void loadValues(URL resource, Locale locale) {
        try {
            load(resource.openStream(), locale);
        } catch (IOException e) {
            throw new RuntimeException("Cannot parse json of file '" + resource.getFile() + "'.", e);
        }
    }

    private void load(InputStream inputStream, Locale locale) throws IOException {
        try (
                inputStream;
                InputStreamReader isr = new InputStreamReader(inputStream)
        ) {
            JsonObject config = JsonParser.parseReader(isr).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : config.entrySet()) {
                if (!registry.contains(entry.getKey())) {
                    registry.register(entry.getKey(), locale, new MessageFormat(entry.getValue().getAsString()));
                }
            }
        }
    }

}

# BrickI18n

An extension for [Minestom](https://github.com/Minestom/Minestom) with a translation api.

## Install

Get the [release](https://github.com/MinestomBrick/BrickI18n/releases)
and place it in the extension folder of your minestom server.

## API

### Maven
```
repositories {
    maven { url "https://repo.jorisg.com/snapshots" }
}
```

```
dependencies {
    implementation 'org.minestombrick.translations:api:1.0-SNAPSHOT'
}
```

### Usage

Check the [javadocs](https://minestombrick.github.io/BrickI18n/)

#### Examples

```java
// Initialize
I18nNamespace namespace = new I18nNamespace(this, Locale.ENGLISH);
namespace.loadValues(namespace, "languages");

I18n.get().register(namespace); // easy access later

// Usage
namespace.send(player, "welcome", sender.getName());

I18nAPI.get().byId("ExtensionName").send(player, "welcome", sender.getName());
```

resources/languages/en.json
```json
{
    "welcome": "Welcome to the server {0}!",
}
```
# Technical Details:

* [Index](INDEX.md)
    * [Technical Details](#technical-details-)
        * [Java](#java)
        * [API](#api)
            * [API Inclusion](#including-)
            * [Usage](#usage-)
                * [JavaDocs](#javadocs)
                * [Code Example](#example)

## Java

Recommended Version: 17

Tested Versions: 16, 17

## API

Do not shade message into your plugin. It will always be present in its plugin form at runtime.

### Including:

<details>
<summary>Maven</summary>

```xml

<repository>
    <id>oskarsmc-repo</id>
    <url>https://repository.oskarsmc.com/releases</url>
</repository>
```

```xml

<dependency>
    <groupId>com.oskarsmc</groupId>
    <artifactId>message</artifactId>
    <version>1.2.0</version>
</dependency>
```

</details>

<details>
<summary>Gradle Kotlin DSL</summary>

```kotlin
maven("https:/repository.oskarsmc.com/releases")
```

```kotlin
implementation("com.oskarsmc:message:1.2.0")
```

</details>

### Usage:

#### JavaDocs

Javadocs are not published anywhere yet. A javadoc jar is published alongside each artifact.

#### Example

```java
import com.google.inject.Inject;
import com.oskarsmc.message.event.MessageEvent;
import com.oskarsmc.message.event.StringResult;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Plugin(
        id = "testvelocityplugin",
        name = "TestVelocityPlugin",
        version = "1.0.0"
)
public class TestVelocityPlugin {
    @Subscribe
    private void MessageEvent(@NotNull MessageEvent event) {
        if (event.originalMessage().contains("poop")) { // Check if message contains a very naughty word.
            event.setResult(StringResult.denied()); // Don't send naughty words to people.
        }
    }
}
```
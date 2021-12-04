# message

Cross-Server Messaging Platform for Velocity with API and LuckPerms integration.

## Commands

### Message:

    /message <player> <message>
    /msg <player> <message>

#### Arguments:

<table>
    <thead>
        <tr>
            <th>Base Command</th>
            <th>Player</th>
            <th>Message</th>
            <th>Permission</th>
        </tr>    </thead>
    <tbody>
        <tr>
            <td><code>/message</code></td>
            <td>Any name of an online player</td>
            <td>Any Message</td>
            <td><code>osmc.message.send</code> (Default)</td>
        </tr>
        <tr>
            <td><code>/msg</code></td>
            <td>Any name of an online player</td>
            <td>Any Message</td>
            <td><code>osmc.message.send</code> (Default)</td>
        </tr>
    </tbody>
</table>

### Reply:

    /reply <message>
    /r <message>

#### Arguments:

<table>
    <thead>
        <tr>
            <th>Base Command</th>
            <th>Message</th>
            <th>Permission</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><code>/reply</code></td>
            <td>Any Message</td>
            <td><code>osmc.message.reply</code> (Default)</td>
        </tr>
        <tr>
            <td><code>/r</code></td>
            <td>Any Message</td>
            <td><code>osmc.message.reply</code> (Default)</td>
        </tr>
    </tbody>
</table>

### SocialSpy:

    /socialspy <on|off|toggle>
    /ss <on|off|toggle>

#### Arguments:

<table>
    <thead>
        <tr>
            <th>Base Command</th>
            <th>Action</th>
            <th>Permission</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><code>/socialspy</code></td>
            <td>Turn On, Off or Toggle</td>
            <td><code>osmc.message.socialspy</code></td>
        </tr>
        <tr>
            <td><code>/ss</code></td>
            <td>Turn On, Off or Toggle</td>
            <td><code>osmc.message.socialspy</code></td>
        </tr>
    </tbody>
</table>

## Configuration

### File:

```toml
# Plugin Settings
[plugin]
    enabled=true
    luckperms-integration=true # If luckperms is found, use luckperms prefixes and suffixes.
    allow-self-message-sending=true # Allow a player to send messages to themselves.

# Customise messages using MiniMessage
# Documentation: https://docs.adventure.kyori.net/minimessage.html#format or https://webui.adventure.kyori.net/
# Default Placeholders: <sender>, <sender_prefix>, <sender_suffix>, <sender_group>, <receiver>, <receiver_prefix>, <receiver_suffix>, <receiver_group>, <message>
# Preview: https://webui.adventure.kyori.net/?mode=chat_closed&input=%3Cwhite%3E%5B%3Ccolor%3A%23FFCE45%3EYOU%3C%2Fcolor%3E%20%E2%86%92%20%3Ccolor%3A%23D4AC2B%3E%3Creceiver_prefix%3E%3Creceiver%3E%3Creceiver_suffix%3E%3C%2Fcolor%3E%5D%20%3Cmessage%3E%3C%2Fwhite%3E%0A%3Cwhite%3E%5B%3Ccolor%3A%23FFCE45%3E%3Csender_prefix%3E%3Csender%3E%3Csender_suffix%3E%3C%2Fcolor%3E%20%E2%86%92%20%3Ccolor%3A%23D4AC2B%3EYOU%3C%2Fcolor%3E%5D%20%3Cmessage%3E%3C%2Fwhite%3E%0A%3Cgradient%3Aaqua%3Ablue%3E%5BSocialSpy%5D%20%3C%2Fgradient%3E%5B%3Cwhite%3E%3Csender%3E%20%E2%86%92%20%3Creceiver%3E%5D%3A%20%3Cmessage%3E&st=%7B%22sender%22%3A%22Player1%22%2C%22receiver%22%3A%22Player2%22%2C%22message%22%3A%22Hello%2C%20World!%22%2C%22receiver_prefix%22%3A%22Admin%20%22%2C%22receiver_suffix%22%3A%22%20%5BLevel%201%5D%22%2C%22sender_prefix%22%3A%22Moderator%20%22%2C%22sender_suffix%22%3A%22%20%5BLevel%200%5D%22%7D
[messages]
    message-sent="<white>[<color:#FFCE45>YOU</color> → <color:#D4AC2B><receiver_prefix><receiver><receiver_suffix></color>] <message></white>"
    message-received="<white>[<color:#FFCE45><sender_prefix><sender><sender_suffix></color> → <color:#D4AC2B>YOU</color>] <message></white>"
    message-socialspy="<gradient:aqua:blue>[SocialSpy] </gradient>[<white><sender> → <receiver>]: <message>"

# Aliases:
# Customise using a TOML string array.
[aliases]
    message=["msg", "tell"]
    reply=["r"]
    socialspy=["ss"]

# Please don't touch this
[developer-info]
    config-version=1.0
```

Preview these values <a href="https://webui.adventure.kyori.net/?mode=chat_closed&input=%3Cwhite%3E%5B%3Ccolor%3A%23FFCE45%3EYOU%3C%2Fcolor%3E%20%E2%86%92%20%3Ccolor%3A%23D4AC2B%3E%3Creceiver_prefix%3E%3Creceiver%3E%3Creceiver_suffix%3E%3C%2Fcolor%3E%5D%20%3Cmessage%3E%3C%2Fwhite%3E%0A%3Cwhite%3E%5B%3Ccolor%3A%23FFCE45%3E%3Csender_prefix%3E%3Csender%3E%3Csender_suffix%3E%3C%2Fcolor%3E%20%E2%86%92%20%3Ccolor%3A%23D4AC2B%3EYOU%3C%2Fcolor%3E%5D%20%3Cmessage%3E%3C%2Fwhite%3E%0A%3Cgradient%3Aaqua%3Ablue%3E%5BSocialSpy%5D%20%3C%2Fgradient%3E%5B%3Cwhite%3E%3Csender%3E%20%E2%86%92%20%3Creceiver%3E%5D%3A%20%3Cmessage%3E&st=%7B%22sender%22%3A%22Player1%22%2C%22receiver%22%3A%22Player2%22%2C%22message%22%3A%22Hello%2C%20World!%22%2C%22receiver_prefix%22%3A%22Admin%20%22%2C%22receiver_suffix%22%3A%22%20%5BLevel%201%5D%22%2C%22sender_prefix%22%3A%22Moderator%20%22%2C%22sender_suffix%22%3A%22%20%5BLevel%200%5D%22%7D">here</a>.

### Don't understand X? Take a look at some docs:

- <a href="https://docs.adventure.kyori.net/minimessage.html#the-components">MiniMessage: The Components</a>
- <a href="https://docs.adventure.kyori.net/minimessage.html#placeholder">MiniMessage: Placeholders</a>

## API

### Including:

#### Maven:

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
 <version>1.0.0</version>
</dependency>
```

#### Gradle Kotlin DSL:

```kotlin
maven("https:/repository.oskarsmc.com/releases")
```

```kotlin
implementation("com.oskarsmc:message:1.0.0")
```

### Usage:

#### JavaDocs

Javadocs are not published anywhere yet. A javadoc jar is published alongside each artifact.

#### Example

```java
import com.google.inject.Inject;
import com.oskarsmc.message.event.MessageEvent;
import com.velocitypowered.api.event.ResultedEvent;
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
        if (event.message().contains("poop")) { // Check if message contains a very naughty word.
            event.setResult(ResultedEvent.GenericResult.denied()); // Don't send naughty words to people.
        }
    }
}
```

## Credits:

- <a href="https://github.com/PaperMC/">@PaperMC</a>
- <a href="https://github.com/KyoriPowered">@KyoriPowered</a>

## Download:

https://github.com/OskarsMC-Plugins/message/releases/

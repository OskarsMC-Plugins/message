# message
Cross Server Messaging Platform for Velocity

## Commands

### Broadcast:

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
            <td><code>osmc.message.send</code></td>
        </tr>
        <tr>
            <td><code>/msg</code></td>
            <td>Any name of an online player</td>
            <td>Any Message</td>
            <td><code>osmc.message.send</code></td>
        </tr>
    </tbody>
</table>

## Configuration
### File:
```toml
# Plugin Settings
[plugin]
enabled=true

# Messages:
# Customise using MiniMessage
# Link: https://docs.adventure.kyori.net/minimessage.html#format
# Placeholders: <sender>, <receiver>, <message>
[messages]
message-sent="<white>[<gradient:red:blue>YOU</gradient> <strikethrough>→</strikethrough> <gradient:blue:red><receiver></gradient>]</white>: <white><pre><message></pre></white>"
message-received="<white>[<gradient:red:blue><sender></gradient> <strikethrough>→</strikethrough> <gradient:blue:red>YOU</gradient>]</white>: <white><pre><message></pre></white>"
```
### Default:
<img src="https://i.imgur.com/H51JW09.png">

### Don't understand X? Take a look at some docs:
* <a href="https://docs.adventure.kyori.net/minimessage.html#the-components">MiniMessage: The Components</a>
* <a href="https://docs.adventure.kyori.net/minimessage.html#placeholder">MiniMessage: Placeholders</a>

## Credits:
* <a href="https://github.com/VelocityPowered/">@VelocityPowered</a>
* <a href="https://github.com/KyoriPowered">@KyoriPowered</a>

## Download:
https://github.com/OskarsMC-Network/message/releases/tag/0.1.0
# message
Cross Server Messaging Platform for Velocity

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

# Messages:
# Customise using MiniMessage
# Link: https://docs.adventure.kyori.net/minimessage.html#format
# Placeholders: <sender>, <receiver>, <message>
[messages]
    message-sent="<white>[<gradient:red:blue>YOU</gradient> <strikethrough>→</strikethrough> <gradient:blue:red><receiver></gradient>]</white>: <white><pre><message></pre></white>"
    message-received="<white>[<gradient:red:blue><sender></gradient> <strikethrough>→</strikethrough> <gradient:blue:red>YOU</gradient>]</white>: <white><pre><message></pre></white>"
    message-socialspy="<gradient:red:blue>SocialSpy: </gradient><white><sender> → <receiver>: <message>"

# Aliases:
# Customise using a TOML string arrray.
[aliases]
    message=["msg", "tell"]
    reply=["r"]
    socialspy=["ss"]

# Error messages:
# Customise using MiniMessage
# Link: https://docs.adventure.kyori.net/minimessage.html#format
# Placeholders: None
[error-messages]
    # General
    no-permission="<red>You do not have permission to execute this command.</red>"
    # Message
    message-player-not-found="<red>That player was not found.</red>"
    message-usage="<red>Usage: /message \\<player\\> \\<message\\></red>"
    # Reply
    reply-no-player-found="<red>Unable to reply.</red>"
    reply-usage="<red>Usage: /reply \\<message\\></red>"

[developer-info]
    config-version=0.3
```
### Default:
<img src="https://i.imgur.com/H51JW09.png">

// More images soon!

### Don't understand X? Take a look at some docs:
* <a href="https://docs.adventure.kyori.net/minimessage.html#the-components">MiniMessage: The Components</a>
* <a href="https://docs.adventure.kyori.net/minimessage.html#placeholder">MiniMessage: Placeholders</a>

## Credits:
* <a href="https://github.com/VelocityPowered/">@VelocityPowered</a>
* <a href="https://github.com/KyoriPowered">@KyoriPowered</a>

## Download:
https://github.com/OskarsMC-Plugins/message/releases/tag/0.2.0
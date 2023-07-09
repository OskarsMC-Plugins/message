# Configuration

- [Index](INDEX.md)
    - [Configuration](#configuration)
        - [File](#file-)
            - [`plugin`](#plugin)
            - [`messages`](#messages)
            - [`error-handlers`](#error-handlers)
            - [`aliases`](#aliases)
        - [Translations](#translations)

## File:

```toml
# Plugin Settings
[plugin]
enabled = true
luckperms-integration = true # If luckperms is found, use luckperms prefixes and suffixes.
miniplaceholders-integration = true # If miniplaceholders is present, you can use the MiniPlaceholders placeholders in the messages.
allow-self-message-sending = true # Allow a player to send messages to themselves.

# Customise messages using MiniMessage
# Documentation: https://docs.adventure.kyori.net/minimessage.html#format or https://webui.adventure.kyori.net/
# Default Placeholders: <sender>, <sender_prefix>, <sender_suffix>, <sender_group>, <sender_server>, <receiver>, <receiver_prefix>, <receiver_suffix>, <receiver_group>, <receiver_server>, <message>
# You may use MiniPlaceholders placeholders if the respective integration option is enabled.
# Preview: https://webui.adventure.kyori.net/?mode=chat_closed&input=%3Cwhite%3E%5B%3Ccolor%3A%23FFCE45%3EYOU%3C%2Fcolor%3E%20%E2%86%92%20%3Ccolor%3A%23D4AC2B%3E%3Creceiver_prefix%3E%3Creceiver%3E%3Creceiver_suffix%3E%3C%2Fcolor%3E%5D%20%3Cmessage%3E%3C%2Fwhite%3E%0A%3Cwhite%3E%5B%3Ccolor%3A%23FFCE45%3E%3Csender_prefix%3E%3Csender%3E%3Csender_suffix%3E%3C%2Fcolor%3E%20%E2%86%92%20%3Ccolor%3A%23D4AC2B%3EYOU%3C%2Fcolor%3E%5D%20%3Cmessage%3E%3C%2Fwhite%3E%0A%3Cgradient%3Aaqua%3Ablue%3E%5BSocialSpy%5D%20%3C%2Fgradient%3E%5B%3Cwhite%3E%3Csender%3E%20%E2%86%92%20%3Creceiver%3E%5D%3A%20%3Cmessage%3E&st=%7B%22sender%22%3A%22Player1%22%2C%22receiver%22%3A%22Player2%22%2C%22message%22%3A%22Hello%2C%20World!%22%2C%22receiver_prefix%22%3A%22Admin%20%22%2C%22receiver_suffix%22%3A%22%20%5BLevel%201%5D%22%2C%22sender_prefix%22%3A%22Moderator%20%22%2C%22sender_suffix%22%3A%22%20%5BLevel%200%5D%22%7D
[messages]
message-sent = "<white>[<color:#FFCE45>YOU</color> → <color:#D4AC2B><receiver_prefix><receiver><receiver_suffix></color>] <message></white>"
message-received = "<white>[<color:#FFCE45><sender_prefix><sender><sender_suffix></color> → <color:#D4AC2B>YOU</color>] <message></white>"
message-socialspy = "<gradient:aqua:blue>[SocialSpy] </gradient>[<white><sender> → <receiver>]: <message>"


# Customise error handling - leave blank to disable
# Documentation: https://docs.adventure.kyori.net/minimessage.html#format or https://webui.adventure.kyori.net/
# Syntax: "class" = "message in minimessage"
[error-handlers]
# "cloud.commandframework.exceptions.InvalidSyntaxException" = "<red><lang:oskarsmc.message.command.exceptions.InvalidSyntaxException></red>"

# Aliases:
# Customise using a TOML string array.
[aliases]
message = ["msg", "tell"]
reply = ["r"]
socialspy = ["ss"]

# Please don't touch this
[developer-info]
config-version = 1.2
```

### Plugin

- `enabled`: (boolean) Enable the plugin
- `luckperms-integration`: (boolean) Enable [luckperms](https://luckperms.net/) integration
- `miniplaceholders-integration`: (boolean)
  Enable [MiniPlaceholders](https://github.com/MiniPlaceholders/MiniPlaceholders/) integration
- `allow-self-message-sending`: (boolean) Allow players to send messages to themselves

### Messages

- `message-sent`: The message sent to the command sender after _sending_ the message
- `message-received`: The message sent to the command sender _receiving_ the message
- `message-socialspy`: The message displayed to a command sender receiving _socialspy_ messages.

### Error Handlers

The format for declaring an error handler is as follows:

```toml
"com.example.exception.ExceptionClass" = "minimessage format"
```

<blockquote>
<details>
<summary>Example: Using exception handlers with translations</summary>
In some cases, server admins may wish to localise their error messages.
For this, the MiniMessage <a href="https://docs.advntr.dev/minimessage/format.html#translatable">translatable</a> tag would be used as seen below:

```toml
"cloud.commandframework.exceptions.InvalidSyntaxException" = "<red><lang:com.yourserver.invalid-syntax>"
```

Accompanied by a matching translation definition for `com.yourserver-invalid-syntax` as a custom definition in the
translation configuration. Read more about the translation configuration [here](#translations)
</details>
</blockquote>

#### Common Exceptions

* `cloud.commandframework.exceptions.InvalidSyntaxException`: Invalid Syntax
* `cloud.commandframework.exceptions.ArgumentParseException`: Invalid Argument
* `cloud.commandframework.exceptions.NoPermissionException`: Permission Error
* `cloud.commandframework.exceptions.InvalidCommandSenderException`: Sender Type Error

### Aliases

* `message`: list\[string] List of aliases for the `/message` command
* `reply`: list\[string] List of aliases for the `/reply` command
* `socialspy`: list\[string] List of aliases for the `/socialspy` command

## Translations

Translations are included as json files, in the `message/translations/` folder.

The plugin will read translations that match the following filename: `translations-<pluginVersion>.json`,
where `<pluginVersion>` is the version of the plugin.

### Translation File

```json
{
  "translation-version": "1.2.0-SNAPSHOT",
  "translations": [
    {
      "language-tag": "en-US",
      "translations": {
        "oskarsmc.message.command.message.argument.player-argument": "The player to send the message to.",
        "oskarsmc.message.command.common.argument.message-description": "The message to send to the player.",
        "oskarsmc.message.command.socialspy.on": "SocialSpy enabled.",
        "oskarsmc.message.command.socialspy.off": "SocialSpy disabled.",
        "oskarsmc.message.command.common.self-sending-error": "You cannot send messages to yourself."
      }
    },
    {
      "language-tag": "es-ES",
      "translations": {
        "oskarsmc.message.command.message.argument.player-argument": "El jugador al cual enviar el mensaje.",
        "oskarsmc.message.command.common.argument.message-description": "El mensaje que se enviara al jugador.",
        "oskarsmc.message.command.socialspy.on": "SocialSpy habilitado.",
        "oskarsmc.message.command.socialspy.off": "SocialSpy deshabilitado.",
        "oskarsmc.message.command.common.self-sending-error": "No puedes enviarte mensajes a ti mismo."
      }
    }
  ]
}
```

* `translation-version`: (string) version of the translation, used to determine if the translation file is outdated or
  not. don't touch this.
* `translations`: (list\[object]) _as follows_
    * `language-tag`: (string) A [language tag](https://www.oracle.com/java/technologies/javase/java8locales.html)
      compliant with [IETF BCP 47](https://en.wikipedia.org/wiki/IETF_language_tag)
    * `translations`: (dictionary\[string, string]) A dictionary of translation keys and values.

<blockquote>
<details>
<summary>Example: Creating custom translations</summary>
In some cases, server admins may wish to create their own localisations, for example, in <a href="#error-handlers">custom error messages</a>.
For this, a custom translation would be defined as such, under translations[].translations

```json5
// note json doesn't support comments like in this demo
{
  "language-tag": "en-GB",
  "translations": {
    // remember to include the default translations as to not break existing commands!
    "com.yourserver.invalid-syntax": "Invalid Syntax"
  }
}
```

Accompanied by a matching error handler for `com.yourserver-invalid-syntax` as a custom exception handler in the
[`error-handlers`](#error-handlers) configuration. Read more about the error handler
configuration [here](#error-handlers)
</details>
</blockquote>
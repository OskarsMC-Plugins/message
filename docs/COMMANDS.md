# Commands

- [Index](INDEX.md)
    - [Commands](#commands)
        - [/message](#message-)
        - [/reply](#reply-)
        - [/socialspy](#socialspy-)

## Message:

    /message <player> <message>
    /msg <player> <message>

| Base Command | Player                       | Message     | Permission                  |
|--------------|------------------------------|-------------|-----------------------------|
| /message     | Any name of an online player | Any Message | osmc.message.send (Default) |
| /msg         | Any name of an online player | Any Message | osmc.message.send (Default) |

## Reply:

    /reply <message>
    /r <message>

| Base Command | Message     | Permission                   |
|--------------|-------------|------------------------------|
| /reply       | Any Message | osmc.message.reply (Default) |
| /r           | Any Message | osmc.message.reply (Default) |

## SocialSpy:

    /socialspy <on|off|toggle>
    /ss <on|off|toggle>

| Base Command | Action                 | Permission             |
|--------------|------------------------|------------------------|
| /socialspy   | Turn On, Off or Toggle | osmc.message.socialspy |
| /ss          | Turn On, Off or Toggle | osmc.message.socialspy |

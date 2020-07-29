# GuildWhitelist
Only allow players in your Hypixel guild to join your Spigot server!

This plugin allows for you to only allow players on your Hypixel guild, and add exceptions to the rule for others if you wish.

### Commands
`/gwl add <username or UUID>` - Add user to exception list.\
`/gwl remove <username or UUID>` - Remove user from exception list.\
`/gwl list` - Show exception list.

<sup><sub>You could also do `/guildwhitelist` but seriously who wants to type that out?</sub></sup>

All above commands require OP or `guildwhitelist.manage`.

### config.yml
```
HypixelApiKey: Run /api new on Hypixel and put the key here.
GuildID: Go to https://api.hypixel.net/guild?name=GUILD-NAME-HERE&key=API-KEY-HERE and paste the "_id" from the response here.
DeniedMsg: "You can put a custom kick message here. Color codes using & work."

# If the plugin fails to get a user's guild (if the Hypixel API is down, or for other reasons), allow users to join the server?
# This could potentially let users not in your guild join!
AllowLoginOnGuildFetchFail: false
```

### Resources Used
[Hypixel Java PublicAPI](https://github.com/HypixelDev/PublicAPI) \
[SparklingComet Mojang API Wrapper](https://github.com/SparklingComet/java-mojang-api)
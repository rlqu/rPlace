# rPlace
The r/Place event from reddit (https://www.reddit.com) but in Minecraft. Messages and other settings are fully customizable.
Go to releases and download the newest version to play it on your server.

It's simple to set up, just drag the .jar-File in your 'plugins' folder. The config.yml-File will be created automatically. 

There you can configure
- spawn location
- teleport to spawn on join
- block place cooldown
- message cooldown
- forbidden blocks/materials
- all messages

Ingame you can configure the spawn location per command (`/setup spawn`) and the worldborder per command aswell (`/setup worldborder <Size>/off`). You also can configure the scoreboard. Just write `null` to get a line not displayed in it. The forbidden materials you can configure in the config.yml-File, just write the item id without the subid and save the file. To apply the changes, simply execute `/reloadplace`, `/rlp` or `/reloadp` in the chat.

The permissions for the commands are
- `/build` > `place.build`
- `/reloadplace` > `place.reload`
- `/setup` > `place.setup`

Alternatively you can just give yourself OP or '*'-rights on your server to get the permissions.
The build mode is to build without cooldown, without forbidden materials and also with right click.

Stay tuned for new updates, I'll upload them here.
Happy easter! 🐰

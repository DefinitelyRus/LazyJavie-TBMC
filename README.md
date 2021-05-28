# LazyJavie
LazyJavie is a multi-purpose utility Discord bot made using Discord JDA. Its primary purpose, as of v1.0, is to host a shop where server members can buy server roles. Server admins can specify which roles are available for purchase and which are not. Newly made roles are automatically added upon using $shop.

## Features
### Highly-flexible shop system
A place where members can buy permissions using their points! ~~The exact methods of earning points and available items are customizable.~~ *That feature was cut from the first release to meet bug-free deadlines.*


### Moderation tools
LazyJavie features ~~an array of~~ common moderation tools, making it more than just a discount MalWart!

### Plenty of other commands
Want to see your latency? How about a list of available commands? Want to see how many points you've earned? ~~Want to get rick-rolled?~~ We got you covered!

## Commands
### Register
	$register <password>
Adds the sender to the database. This allows the server to track user activity inside the server. As of v1.0, the bot only tracks how many messages were sent by the user while the bot is online.

### Deregister
	$deregister <password>
Removes the sender from the database.

### Shop
	$shop <subcommand> <arguments>
Allows members to buy rewards using points they have accumulated during their stay in the server. As of version 1.0, members can only buy server roles as rewards. This will be updated later on to allow for more rewards to be set by the server admins. BUY is also the only sub-command under the SHOP command. More will be added in later updates.
- `$shop`: Lists the available items and their prices.
- `$shop buy <role>`: Gives the sender the requested role given that the role is available and the sender has enough points.

### Admin Shop
	$ashop <subcommand> <arguments>
An admin-only command where administrators can change the settings of their server's shop.

Available subcommands are as follows:
- `$ashop blacklist <role>`: This command removes a role from the shop inventory, preventing players from purchasing certain roles.
- `$ashop setprice <role> <price>`: Updates the price of a certain role.
- [TO BE ADDED] `$ashop setstock <role> <stock>`: Updates how many times a role can be purchased.
- [TO BE ADDED] `$ashop setdesc <role> <description>`: Updates the product description to be displayed.

### Message Console
	$msgc <message>
Sends a message directly to the console.

### Quit
	$quit <required argument>
   - Turns the bot offline.
    
## How it works
[Ask Rus for an explanation]
 
## Installation
See the [LazyJavie releases page](https://github.com/DefinitelyRus/LazyJavie/releases) for detailed instructions fit for the version you're interested in using.
 
## How to invite
[Leave this empty for now]
 
## Plans
### Future updates
The general progress of this project can be seen on our [Trello Roadmap](https://trello.com/b/N6bLfnaB/lazyjavie-roadmap). Although you can always join our [Discord server](discord.gg/bZ728v4)'s announcement channel for updates about this project, among other projects unrelated to this one.

Despite this, here's a list of priority updates we have planned:
- [ ] More shop customizability
- [ ] Host GUI (and by extension, even more customizability!)
- [ ] More and flexible moderation tools

### Target goal
We want to release the bot as a server-hosted bot similar to [Mee6](https://mee6.xyz) and [Rythm](https://rythm.fm), but on a much smaller scale. There are simply too many bots doing the same thing, we simply cannot compete unless we pull off something amazing.

## Code Contributors
### Head Development Team
- [@DefinitelyRus](https://github.com/DefinitelyRus)
- [@fancybaby404](https://github.com/fancybaby404)

### Documentation
- [@hansl04](https://github.com/hansl04)
- [@triciafaye](https://github.com/triciafaye)

## License
This project uses GNU General Public License v3.0. See LICENSE for more information.
 
## Trivia
[Leave this empty for now]

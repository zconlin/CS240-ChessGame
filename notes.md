## Java Notes

*I've never coded in Java before tackling this project, so this is a page for me to take notes on the Java principles as I learn them. It may get used and it may not, we'll have to see.*

---

### SQL Commands Used

use chess;

create table chess.auth
(
authToken text        null,
username  varchar(24) null
);

create table chess.game
(
gameID        int        auto_increment,
whiteUsername varchar(24) null,
blackUsername varchar(24) null,
spectators    text null,
gameName      varchar(24) null,
game          text null
);

create table chess.user
(
username varchar(24) null,
password varchar(24) null,
email    varchar(24) null
);


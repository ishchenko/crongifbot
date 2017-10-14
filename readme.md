# Telegram crongifbot

[@crongifbot](https://t.me/crongifbot)

Posts giphy for random tag from list on cron schedule

## Configuration 

Manually create `jobs` directory in your working directory, put list of json files there. 
Each filename is telegram `chatId`, negative for chat rooms, positive for individual chats.
You must somehow know your `chatId` in advance. No means for configuration from chats, sorry.

Json example:
```json
[
  {
    "cron": "0 55 11 ? * MON-FRI",
    "tags": [
      "food", "dinner", "fastfood", "eat", "meal", "hunger", "cuisine", "snack", "lunch", "chew", "chewing"
    ]
  }
]
```
The `jobs` directory is scanned every minute with `org.quartz.jobs.DirectoryScanJob`. If changes are 
detected, chat jobs would be rescheduled with new settings.

## Running

Pass bot token as command line parameter: `java -jar crongif-1.0-SNAPSHOT.jar <token>`


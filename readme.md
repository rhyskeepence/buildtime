Build time server
=================

Sometimes, justifying hardware upgrades can be difficult. It is easier if you can quantify the benefits. So, have your build tool keep track of how much time it is spent running each day across all development machines.

# Assumptions
- Time spent in your build tool is time that you aren't being productive.
- If your build takes 10 seconds, then you probably aren't getting too distracted. I'd only start using this tool if you spend more than a minute per build.

# The nitty gritty

This is a simple server, which listens for UDP requests in the following format:

    user:(\w+) host:(\w+) directory:([\w\-\/]+) command:'(.*)' time:(\d+) processor:([\d\.]+)% memory:(\d+) input:(\d+) output:(\d+) received:(\d+) sent:(\d+) waits:(\d+) os:(\w+) cpu:([\w\d]+) ncpu:(\d+)

The amount of processing time, along with the command name, date/time, hostname, and so on, are stored in a mongo database.

A client script is included, which can be incorporated into your build script (ie, mvn/ant/sbt/etc) for all developers on the project.

I'll be building some scripts to aggregate this data into a daily figure of time spent building.


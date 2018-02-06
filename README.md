# Habrewles

Habrewles is still very, very, very work-in-progress.

## What is this?
Habrewles is a Finite State Machine that can control your SmartHome

Features:
  * States can issue commands on enable and disable
  * Commands can be any Actions using the implemented technologies
  * The active State changes on conditions called "transitions"
  * Transitions can be listening for any Event using the implemented technologies
  * Multiple States can be active
  * Nested State-Machines are possible (During one State is active, another State-Machine
  is running and stops as soon as this State gets inactive)

Technologies:
  * MQTT-Events
  * HTTP-Events
  * Timer-Events
The Technologies can easily be extended and will be extended in the future

## Why this?
I was working with Smarthome-related-stuff for a while now and i
discovered that there are a lot of nice tools out there. Nearly
every company has its own tools, hubs and systems, so you need
some general bus to control everything. This repository is not
that. There are good solutions for this problem out there, like
[openHAB](https://www.openhab.org/) or proprietary solutions.

My Problem with openHAB or other solutions was, that they don't really
make your home **smart**. They only let you control everything yourself.
But isn't the most important Part of a SmartHome, that the home itself
is smart? openHAB offers a pretty nice solution  for that too. A so called
"Rule-Engine". You can create simple If-This-Than-That rules or even
more advanced ones with their scripting language.

But what if you want to make something like this:

After 8 pm I want the lights to turn on as soon as the sun is down.
I want to switch different scenarios on will, maybe a happy one with
blue-glowing lights and happy music, maybe a comfy one with reddish-flickering
lights to simulate a fire and calming music. At 11 pm I want to switch
the lights to a dark-red glow as a sign to go to bed and when I am in bed
I want to switch them on and off with the click of a physical button.

As a human being I am lazy. I don't want to learn a new scripting-API.
And if I wasn't into programming I wouldn't want to have to learn programming
only for my SmartHome.
As a programmer I don't want to hardcode complex rules. What if I got a
new light? Do I have to recode all rules? Or what if I want to alter the
times? Do I have to change 20.000 ifs?

This type of rules calls for a struct called "Finite-State-Machine" but
a FSM is a lot of work to code with simple ifs.

Using Habrewles you can easily create states and transitions between them.
States can issue MQTT-Commands (which allows Habrewles to work with openHAB),
send HTTP-Requests and much much more.
Transitions are the conditions, on which the states change from one to another.
That can be waiting for a HTTP-Request or an MQTT-Command
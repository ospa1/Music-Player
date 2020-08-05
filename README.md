# Music-Player
music player service and a client

# Design
the project come in two parts the first is a music service called clipServer that contains the music playing interface and resources.
the second is the audio client that binds to the service and can play, pause, stop, and choose music or audio files.

Pros:

the clip server can be bound to multiple apps.
audio will continnue playing even if the phone is not in the foreground or is locked.

Cons:

have to manually add music to the service.
selecting music should be a scrollable list.

# How to Install
import the clip server project first in android studio. then run it on a virtual device or connected android phone.
then import the audio client into android studio and run it like previous.
the clipserver must be installed or ran first before the audio client.

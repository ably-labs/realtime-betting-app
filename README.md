Dependable realtime betting app 
================================

This demo demonstrates a simple way to engineer a dependable realtime betting app with Confluent Cloud and Ably that can reliably scale to handle any number of concurrent users. 

Tech stack
----------

The following main components are used for building this demo:

-   [Confluent Cloud (Kafka and ksqlDB)  ](https://www.confluent.io/confluent-cloud/)for event streaming and stream processing. 

-   [Ably Kafka Connector](https://github.com/ably/kafka-connect-ably), for transferring data from Confluent Cloud topics into Ably channels.

-   [Ably](https://ably.com/), for distributing odds and push notifications to end-users, and streaming bets they make into Confluent Cloud - all in realtime, at any scale.  

-   Android app, for users to view odds as they change, and place bets in realtime.

To build this demo, you will have to [sign up for a free Ably account](https://ably.com/signup). You will also need a Confluent Cloud account. 

How it works
------------

For  details about building the betting app, [check out the post on the Confluent blog](https://www.confluent.io/blog/real-time-betting-platform-with-confluent-cloud-and-ably/).

If you have any questions, [reach out to us](https://ably.com/contact)!

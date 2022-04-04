# Mobile-betting-app
Dependable realtime betting app 
This demo demonstrates a simple way to engineer a dependable realtime betting app with Confluent Cloud and Ably that can reliably scale to handle any number of concurrent users. 
Tech stack
The following main components are used for building this demo:
Confluent Cloud (Kafka and ksqlDB) for event streaming and stream processing. 
Ably Kafka Connector, for transferring data from Confluent Cloud topics into Ably channels.
Ably, for distributing odds and push notifications to end-users, and streaming bets they make into Confluent Cloud - all in realtime, at any scale.  
Android app, for users to view odds as they change, and place bets in realtime.

To build this demo, you will have to sign up for a free Ably account. You will also need a Confluent Cloud account. 
How it works


For more details about building the betting app, check out the post on the Confluent blog. 

If you have any questions, reach out to us!

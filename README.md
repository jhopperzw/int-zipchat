### Hackathon Project: ZipChat

ZipChat is a Zipwhip Integration designed to function like a Slack Channel.
It maintains a list of Channels, and maintains a list of Subscribers in each Channel.
When the Zipwhip landline receives an incoming message ZipChat performs the following:
* verify the message source is a member of a Channel
* determine which Channel the user is a Subscriber of
* prepend the username to the message
* send the message to all members of that Channel

### Keywords

/JOIN channel

/LEAVE

/CREATE channel

/DELETE channel

/ADDUSER displayname

### Current Features

ZipChat supports creating multiple Channels, however a user may only be subscribed to one Channel at a time.
A user must be subscribed to a Channel in order for the messages they send to the landline to be sent to all
Channel subscribers.

### Future Features

1. ZipChat Integration Settings page will support Channel management (to create, rename, and delete Channels)
and Subscriber management (delete Subscribers, ban Subscribers preventing them from rejoining).

2. Additional keywords:

/SUBSCRIBERS

sends a list of all Channel members with their phone number.

/CHANNELS

sends a list of all Channels.

/HISTORY MessageCount

sends Channel messages from prior to the Subscriber joining.

/INVITE mobilenumber

allows a Subscriber to have ZipChat invite someone to the Channel.

/HELP

sends a message with all valid keywords.

/DESCRIBE

send a message with the Channel description.

/RENAMEUSER NewDisplayName

allows a Subscriber to update their display name.

/RENAMECHANNEL NewChannelName

allows landline owner to update the channel name.

### Potential Bluesteel Support

* The Zipwhip Saas App may consider adding functionality to the Message Feed allowing messages to be
organized by Channel. This would permit a subscriber to join multiple Channels and easily read all
messages of a single Channel.

### Running ZipChat Locally

To run ZipChat locally you must clone https://github.com/Zipwhip/int-local-env and follow the steps
in the readme.md to stand up the base services. Then modify the configuration to include ZipChat

#### Prerequisites

1. VPN
2. local mongodb instance and ip address provided in startup script below
3. local docker image from `mvn clean package docker:build`

#### Run Docker Container

	docker run -p 8080:8080 -e LOGSTASH_ENDPOINT=ae1ba77558b7211e89ea30610e8ac6c8-82f8ef7d85a07e55.elb.us-west-2.amazonaws.com -e LOGSTASH_PORT=5019 -e LOGSTASH_BUFFER=262144 -e SPRING_CLOUD_CONSUL_ENABLED=false  -e kafka.hosts=zw-preprod-kafka-bootstrap.us-west-2.aws.zwpreprod.io:9094 -e kafka.autoCommit=false  -e SERVICE_NAME=int-zipchat-service -e spring.data.mongodb.uri=mongodb://${LOCAL_IP}:27017 -e SPRING_DATA_MONGODB_DATABASE=integrations -e spring.profiles.active=test -e zipwhip.int-manager.url=https://int-gateway.us-west-2.aws.zwpreprod.io -e spring.application.name=int-zipchat -e zipwhip.toolkit.url.bs=https://toolkit.us-west-2.aws.zwpreprod.io  zipwhip/int-zipchat-service:latest

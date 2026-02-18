# RMI based Chat App

## About the project

The project includes modules for server and client side, as well as the module with shared interfaces. 

The server module allows for running the server side of the chat application, with basic persistence logic for message history and message dispatching/coordination.

The client module includes basic javax GUI and rmi calls needed for the chat app users to communicate with the server (RMI calls to the server for sending messages, leaving/joining/creating chat rooms).

## Building and running the components
The java archives for either of them can be obtained by running 'mvn clean package' (with maven installed on the machine) from the root directory of the project.

The current implementation requires explicit launching of the rmiregistry via command:

    rmiregistry <port> -J-classpath -J/<path_to_chat-common-1.0-SNAPSHOT.jar>

### Server 
In order to run the server, the following command should be executed:

    java -jar <path_to_chat-server-1.0-SNAPSHOT-shaded.jar> <port>

Upon the successful completion of this command execution, 'Server ready' must appear in the terminal output.

### Client
In order to start the client, use command:

    java -jar <path_to_chat-client-1.0-SNAPSHOT-shaded.jar> <host> <port>

Upon the successful completion of this command execution, a window for username input must appear on the screen.

The port parameter for all the three commands has to be the same.


javac -cp ".:h2-2.2.224.jar" *.java compiles all .java files to .class
java -cp ".:h2-2.2.224.jar" Server creates the database if it doesnt exist already and starts the server 
java -cp ".:h2-2.2.224.jar" ClientMain runs the client 

# SPADGIFY

This is our simplified take on how to implement a distributed audio streaming service. To really understand this implementation we recommend that you read the provided report that can be found in this repository (5dv205_project_report.pdf).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

The project was written using Java 18.0.1.1, Maven 3.9.9 and libprotoc 28.0. Hence, these dependencies are needed in order to install and use the software.

In order to use the provided React client, the node JS runtime environment is needed. During the development of this client the version used was: v22.9.0.


### Installing

For installing the software we refer to section 2 (Usage) in the report (5dv205_project_report.pdf) that can be found in the repository. Just to clarify, the m-bit identifier argument for a node also defines the size of the chord cluster. If m is set to 3, then the max number of nodes will be $2^m = 2^3 = 8$ nodes. The value given to m must be the same for all nodes in the cluster.

```
Usage: chord [-h] [-c=<cache>] [-d=<delay>] [-i=<remoteIp>][-p=<remotePort>] mode port m
```

for more information, read section 2 in the report.

## Running the tests

The repository has a test client that can upload music files to the cluster as well as performing logic and performance tests. For information on how to use this test client we refer to section 2.2 in the report.

### Test details

For a more detailed explanation of the tests, please read section 9 in the report. 

## Deployment

Add additional notes about how to deploy this on a live system

To deploy a new cluster a user can do the following:


1. open a terminal and run the jar file with the following command: 
```
java -jar spadgify-1.0-SNAPSHOT.jar -c 10 -d 1000 1 8185 3
```
Say that the machine running this node has the ip: 192.168.38.126

2. To add nodes to the cluster, open a new terminal and run the jar file with the following command:
```
java -jar spadgify-1.0-SNAPSHOT.jar -c 10 -d 1000 -p 8185 -i 192.168.38.126 0 8187 3
```
Worth noting is that the ID of a node is a hash of the nodes ip and port. When m is a small number, the likelyhood of collsions is higher. Therefore each node needs to have a unique ID in order to join the cluster. If a connecting node fails, try changing the port to get a different hash or increase the size of m.

A user can add music to the cluster by running the test client as described in section 2.2 in the report with the following run configuration:

```
-u ./../../testMedia/input-music 192.168.38.126 8185 3 192.168.38.126 8080 8000
```
in this case we are sending the data to a node in the cluster with the IP and port of 192.168.38.126:8185 but it could be any node in the cluster.

To test the audio streaming a user can start the provided REACT client by opening a terminal, moving into the SPADGIFY/react/my-spadgify-app folder. Then performing the following two commands:

1. npm install
2. npm run dev

this will start the react client on localhost:SOME_PORT. The user can then enter the IP of some node in the cluster to connect to. 

## Authors

* **Anton Dacklin Gaied** - [Anton](https://github.com/ubbe92)
* **Sinthujan Ponnampalam** - [Sinthu](https://github.com/Sinthu01)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* Inspiration from the original article of [Chord](https://pdos.csail.mit.edu/papers/chord:sigcomm01/chord_sigcomm.pdf)
* The associate professor and scientist Per-Olov Östberg [P-O](https://www.umu.se/personal/per-olov-ostberg/)
* The TA and postdoctoral Chanh Le Tan Nguyen  [Chanh](https://www.umu.se/personal/chanh-nguyen/)
* Spotify [Spotify](https://open.spotify.com/)
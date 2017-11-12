# Débattons

> Methodical debate platform based on user empowerment

"Débattons" is a French word that means "Let's debate".

The aim of this debate platform is to trust users to level up the discussions quality, so that from collective knowledge would emerge best responses to every question put in debate. It is a sort of Wikipedia for debates.

For this to happen, users will be able to learn debate methods using the platform. They will also be given tools to filter out and go through discussions in their own ways.

The more they participate and are recognized by others, the more they will be granted power inside the platform.

## Resources

The project is led by [iORGA Group](http://www.iorga.com) in the way we hope to be the most open (don't hesitate to participate, or send us your remarks or suggestions).

Here are some resources:
 * Current [**development and organization status**](https://trello.com/b/MfS0wzzJ/wikip%C3%A9dia-du-d%C3%A9bat) (currently only in French language)
 * A more general [**presentation of the project and its ideas**](https://docs.google.com/presentation/d/1UIsnLdP2XgO_Ii6g98lWW4FsMuDccD-TigsT5NSFKOU/edit#slide=id.g224b5ac09f_1_0) (currently only in French language)

## Installation of development environment

Débattons uses the following tools that you should install first:
 * Java SE Development Kit 8 ([JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html))
 * [Maven](https://maven.apache.org/) 3+
 * [Node.js](https://nodejs.org/en/) v6.11.3 LTS
 * [Angular CLI](https://cli.angular.io/) (that you can simply install with `npm install -g @angular/cli`)
 * [Docker](https://docs.docker.com/engine/installation/)
 * [Docker Compose](https://docs.docker.com/compose/install/)

Here are the steps to launch the environment:
```bash
# First launch the OrientDB server using Docker
# On Windows, if you are using Docker Toolbox, execute this using the Docker Console. If you are not in your own user's path, don't forget to declare the current path as a shared folder in VirtualBox
./docker/cmd.sh build-and-run --only-orientdb
```
The first time, you will have to configure the DB:
 * Connect to [http://localhost:2480/studio/index.html](http://localhost:2480/studio/index.html) (if you are on Windows < 10 and installed Docker Toolbox, replace `localhost` with `192.168.99.100`) and click on "New DB" and create the Database named `debattons` with the user `root` and the password that you will find in the file `docker-data/conf/debattons.env` in the project file tree.
 * Then create a new User on that database named `api-server` with the password `password`, the `admin` role and the status `Active`

Now launch the Webservices part
```bash
cd api-server
mvn spring-boot:run
```
Now in another shell you can start the Angular UI:
```bash
cd ui
npm install
ng serve
```

You can finally access to the platform at [http://localhost:4200](http://localhost:4200)

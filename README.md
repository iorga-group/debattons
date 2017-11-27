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

## Install & run development environment
### Using Docker
#### On Ubuntu 16.04 without Docker installed
The following command will install Docker and Docker Compose, and finally will clone this project to `/opt/debattons`.
```bash
curl -L "https://raw.githubusercontent.com/iorga-group/debattons/master/setup/run-env-on-ubuntu-16.04.sh" > /tmp/setup-debattons-run-env-on-ubuntu-16.04.sh && bash /tmp/setup-debattons-run-env-on-ubuntu-16.04.sh
```
Now restart your session (in order for the group changes to take effects, your user has been added to `docker` group in order to be able to execute Docker) or execute the following command:
```bash
su -lp $USER
```
#### On Linux box with Docker already installed
Clone the current project to `/opt/debattons` or let the following command do that for you:
```bash
curl -L "https://raw.githubusercontent.com/iorga-group/debattons/master/setup/debattons-git-copy.sh" > /tmp/debattons-git-copy.sh && bash /tmp/debattons-git-copy.sh
```
#### Build and run
Now you are able to issue the following command that will create all the development environment into a Docker image and then will run this image as well as the required database.
```bash
/opt/debattons/docker/cmd.sh build-and-run
```

You are now able to access Débattons at [http://localhost:4200](http://localhost:4200)

You will be able to access OrientDB Studio at [http://localhost:2480/studio/index.html](http://localhost:2480/studio/index.html) (if you are on Windows < 10 and installed Docker Toolbox, replace `localhost` with `192.168.99.100`)

### Using Vagrant
The following procedure will automatically create, setup and configure the development environment in a dedicated virtual machine:
1. Install [Vagrant 2.0+](https://www.vagrantup.com/).
1. Install [VirtualBox 5.0+](https://www.virtualbox.org/).
1. Clone this project.
1. Enter the Vagrant configuration directory: `cd debattons/vagrant`.
1. Create and provision the virtual machine: `vagrant up`.
 * during this step, a new dedicated virtual machine will be created, setup and all the dependencies will be installed;

At your first run, Débattons will build and start automatically, at next run you will have to start the server manually.

``` bash
vagrant ssh
/opt/debattons/scripts/build-and-run.dev.sh --start-orientdb-server
```

### Install the development environment automatically on your Ubuntu 16.04 (without using Docker nor Vagrant)
The following command will install all the dependencies of Débattons as well as itself on your system.
```bash
curl -L "https://raw.githubusercontent.com/iorga-group/debattons/master/setup/dev-env-on-ubuntu-16.04.sh" > /tmp/setup-debattons-dev-env-on-ubuntu-16.04.sh && bash /tmp/setup-debattons-dev-env-on-ubuntu-16.04.sh
```
Débattons will be installed in `/opt/debattons` and OrientDB in `/opt/orientdb`.

The following script will start OrientDB, create the required DB & user if needed, and start Débattons:
```bash
/opt/debattons/scripts/build-and-run.dev.sh --start-orientdb-server
```

### Manual installation of development environment

Débattons uses the following tools that you should install first:
 * Java SE Development Kit 8 ([JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html))
 * [Maven](https://maven.apache.org/) 3+
 * [Node.js](https://nodejs.org/en/) v6.11.3 LTS
 * [Yarn](https://yarnpkg.com) (that you can simply install with `npm install -g yarn`)
 * [OrientDB](https://orientdb.com/getting-started/)

Here are the steps to launch the environment (replace `$PATH_TO_ORIENTDB` with the path to your OrientDB installation):
```bash
# First launch the OrientDB server
$PATH_TO_ORIENTDB/bin/server.sh
```
The first time, you will have to configure the DB:
 * Connect to [http://localhost:2480/studio/index.html](http://localhost:2480/studio/index.html) and click on "New DB" and create the Database named `debattons` with the user `root` and the password that you will find in the file `docker-data/conf/debattons.env` in the project file tree.
 * Then create a new User on that database named `api-server` with the password `password`, the `admin` role and the status `Active`

Now launch the Webservices part in another shell:
```bash
cd api-server
mvn spring-boot:run
```
Now in another shell you can start the Angular UI:
```bash
cd ui
yarn install
./node_modules/.bin/ng serve
```

Those 3 last steps (DB creation & launch the webservices & UI) can be done automatically with:
```bash
/opt/debattons/scripts/build-and-run.dev.sh
```

## Access Débattons in your development environment

You can finally access to the platform at [http://localhost:4200](http://localhost:4200)

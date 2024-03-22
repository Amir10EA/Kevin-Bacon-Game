# Kevin Bacon Game

## Overview

The Kevin Bacon game is a Java application that explores the "six degrees of separation" concept within the film industry, specifically focusing on connecting various actors to Kevin Bacon through a series of shared movie appearances. Utilizing a graph-based approach, this application builds a network of actors and movies from a provided dataset and then calculates the shortest path from any actor back to Kevin Bacon.

## Authors

- **Amir Eihab El Abidou**
- **Arianit Paso**
- **Omar Almassri**

## Features

- Builds a graph from a given dataset containing actors and movies.
- Finds the shortest path from any specified actor to Kevin Bacon using a breadth-first search algorithm.
- Interactive user console for querying actor connections to Kevin Bacon.

## Requirements

- Java Development Kit (JDK) 8 or above.

## Setup and Running the Application

1. **Clone the Repository**

   Clone or download this repository to your local machine.
   git clone https://github.com/Amir10EA/Kevin-Bacon-Game

2. **Prepare the Dataset**

Due to the large size of the movie dataset, it is not included in this repository. You will need to obtain or create a dataset file formatted correctly:
- Each actor entry should start with `<a>` followed by the actor's name.
- Each movie entry should start with `<t>` followed by the movie title.
- Place the dataset file in an accessible directory on your local machine.

3. **Compile the Application**

Navigate to the directory containing the `KevinBaconGame.java` file and compile it using the Java compiler.
javac KevinBaconGame.java

4. **Run the Application**

Run the compiled application, specifying the path to your dataset file as an argument.
java KevinBaconGame

Follow the on-screen prompts to enter actor names and discover their connection to Kevin Bacon.

## Contributing

Contributions to the Kevin Bacon game are welcome! If you have suggestions or encounter issues, please feel free to open an issue or create a pull request on the repository.

## License

This project is open-source and available under the MIT License.

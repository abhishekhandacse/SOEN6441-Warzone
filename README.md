# WarZone

## Introduction
This project involves the development of a complex Java program for playing the game of Risk. The project is designed to be undertaken by teams of 5 or 6 members. It consists of building a challenging Java program that simulates the game of Risk, including the rules, map files, and command-line gameplay.

## Problem Statement
The specific project for this semester is to build a simple "Risk" computer game. The goal is to create a program that is compatible with the rules and map files of the "Warzone" version of Risk, which can be found at [Warzone](https://www.warzone.com/).

## Directory Structure

```
├── documentation/              <- All project related documentation and reports
├── src/                        <- Source code for the project
│  ├── main/
│  │  ├── java/
|  |  |  ├── Controller/        <- Controller classes
|  |  |  ├── Exceptions         <- Exception Handling classes
|  |  |  ├── Logger/            <- Logging classes
|  |  |  ├── Models/            <- Model classes
|  |  |  ├── Utils/             <- Utility classes
|  |  |  ├── View/              <- View classes
|  |  ├── maps/                 <- 
│  ├── test                     <- 
│  │  ├── java/
|  |  |  ├── Controller/        <- Controller Tests
|  |  |  ├── Models/            <- Model Tests
|  |  |  ├── Utils/             <- Utility Tests
|  |  |  ├── View/              <- View Tests
├── .gitignore                  <- List of files and folders git should ignore
└── README.md                   <- The top-level README for developers using this project 
```
### Map
- The game map is represented as a connected graph, where each node represents a country owned by a player.
- Edges between nodes represent adjacency between countries.
- Continents are connected subgraphs of the map, and each continent has a control value that determines the number of armies given to a player who controls all of it.
- Players can define custom connected graphs for gameplay.

## Project Components
The completion of the project is divided into three separate components:
1. **First and Second Intermediate Project Delivery:** These are intermediate operational builds of the software, effectively demonstrating the full implementation of important software features.
2. **Final Project Delivery:** This is the demonstration of the finalized version of your software. It should also showcase the effective use of Java features presented in lectures and the tools introduced during the project.

All project deliveries are presented in the laboratory, where the team demonstrates the implemented features to the TAs following a pre-circulated grading sheet.

## Contributors
- [Abhishek Handa](https://github.com/abhishekhandacse)
- [Harman Singh Jolly](https://github.com/coderjolly)
- [Rajat Sharma](https://github.com/rajatjc)
- [Amanpreet Singh](https://github.com/amanpreetbatra)
- [Anurag Teckchandani](https://github.com/anurag444)

## References
- [Warzone](https://www.warzone.com/)

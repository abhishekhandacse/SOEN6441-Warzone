# SOEN6441-Warzone
SOEN-6441 Course Project Warzone Game

# Risk Game Project README

## Table of Contents
- [Introduction](#introduction)
- [Problem Statement](#problem-statement)
- [Project Components](#project-components)
- [Features](#features)
- [How to Play](#how-to-play)
- [Contributors](#contributors)
- [References](#references)

---

## Introduction
Welcome to the Risk game project! This project involves the development of a complex Java program for playing the game of Risk. The project is designed to be undertaken by teams of 5 or 6 members. It consists of building a challenging Java program that simulates the game of Risk, including the rules, map files, and command-line gameplay.

## Problem Statement
The specific project for this semester is to build a simple "Risk" computer game. The goal is to create a program that is compatible with the rules and map files of the "Warzone" version of Risk, which can be found at [Warzone](https://www.warzone.com/).

### Map
- The game map is represented as a connected graph, where each node represents a country owned by a player.
- Edges between nodes represent adjacency between countries.
- Continents are connected subgraphs of the map, and each continent has a control value that determines the number of armies given to a player who controls all of it.
- Players can define custom connected graphs for gameplay.

### Game
- The game starts with a setup phase where the number of players is determined, and territories are randomly assigned.
- The main play phase is turn-based, where players issue orders:
  - Deploy: Place armies on a player's territories.
  - Advance: Move armies between adjacent territories, including attacks on enemy territories.
  - Special Orders: Including bomb, blockade, airlift, and negotiate.
- Players receive reinforcement armies at the beginning of their turns based on territory ownership.
- Successful conquests during a turn earn players cards with various effects.
- Players can play cards during their turn to influence the game.

### Winning Condition
The game ends when one player owns all the territories on the map.

## Project Components
The completion of the project is divided into three separate components:
1. **First and Second Intermediate Project Delivery:** These are intermediate operational builds of the software, effectively demonstrating the full implementation of important software features.
2. **Final Project Delivery:** This is the demonstration of the finalized version of your software. It should also showcase the effective use of Java features presented in lectures and the tools introduced during the project.

All project deliveries are presented in the laboratory, where the team demonstrates the implemented features to the TAs following a pre-circulated grading sheet.

## Features
- Customizable game maps.
- Turn-based gameplay with deployment, advancement, and special orders.
- Card system with various game-altering effects.
- Simulation of battles and conquests.
- Dynamic reinforcement calculations.
- Player elimination and victory conditions.

## How to Play
1. Clone the project repository.
2. Build and run the Java program.
3. Follow on-screen instructions to set up the game and make your moves.
4. Conquer the world and achieve victory!

## Contributors
Abhishek Handa
Harman Singh Jolly
Rajat Sharma
Amanpreet Singh
Anurag Teckchandani

## References
- [Warzone](https://www.warzone.com/)
- Domination game maps

Feel free to explore the project further and contribute to its development! Enjoy playing Risk!

# Packing Puzzle Solver

A Java implementation of a 2D packing puzzle solver with an interactive GUI. This application solves the classic problem of fitting various shaped pieces onto a board without overlapping.

## Problem Description

The packing puzzle consists of:

- A square board (default 4x4).
- A collection of puzzle pieces of different shapes.
- Each piece can be rotated in 90-degree increments.

The goal is to determine if the given set of pieces can be arranged on the board so that:
- All pieces fit within the board boundaries.
- No pieces overlap.
  
## Puzzle Pieces

The program supports 11 different piece shapes:

<img width="1000" alt="Possible pieces" src="https://github.com/user-attachments/assets/f546b42d-dc1c-4b98-bbb5-1b9b7d95087e" /> 
Each piece is represented by a unique number (1-11) and can be rotated to any of the four possible orientations (0°, 90°, 180°, 270°).

## Features

- **Interactive GUI**: For visualizing the puzzle and solutions.
- **Piece Selection**: Left-click on a piece to add it to your selection, right-click to remove it.
- **Customizable Board Size**: Enter your desired board size.
- **Solution Visualization**: The solution is displayed with colored pieces on the board.
- **Backtracking Algorithm**: Uses a backtracking algorithm to find a valid packing arrangement.

## How It Works

The core algorithm uses a **backtracking approach** to solve the puzzle:

1. Try to place each piece at every possible position on the board.
2. For each position, try all four possible rotations (0°, 90°, 180°, 270°).
3. If a piece can be placed without overlapping:
   - Place it and recursively try to place the next piece.
4. If all pieces can be placed, a solution is found.
5. If no valid placement exists, remove the piece and try the next position or rotation.

### Running the Application
To compile and run the application:

```bash
javac PackingPuzzle.java PackingPuzzleGUI.java
java PackingPuzzle
```

## Using the GUI
- **Select Pieces:** **Left-click** on a piece to add it to your selection. **Right-click** on a piece to remove it from your selection.  
- **Set Board Size:** Enter the desired board size in the "Board Size" field (default is 4x4).  
- **Solve the Puzzle:** Click the **"Solve Puzzle"** button to attempt solving the puzzle with the selected pieces.  
- **Clear Selection:** Click the **"Clear Selection"** button to reset the current piece selections and start fresh.  
  
Once the solution is found, it will be displayed on the left side of the application. Each selected piece will show how many pieces of that type you’ve chosen, allowing for better puzzle management and visualization.

## Implementation Details

### Core Components

#### `PackingPuzzle.java`
- Contains the core solving algorithm.
- Defines the shapes of all possible puzzle pieces.
- Implements backtracking search with rotation handling for each piece.

#### `PackingPuzzleGUI.java`
- Provides the interactive graphical interface for users to select pieces and visualize solutions.
- Manages user interaction for piece selection, board configuration, and solution visualization.
- Displays the solution with colored pieces representing different puzzle pieces.

## Algorithm Complexity

The worst-case time complexity of the backtracking algorithm is **O(4^n)**, where **n** is the number of pieces, as we need to try each piece in all four orientations at each position on the board. However, the actual runtime is typically much better than this due to early pruning of invalid placements, which helps reduce the number of recursive calls.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class PackingPuzzleGUI extends JFrame {
    private int[][] board;
    private int[][][] pieceDictionary;
    private BoardPanel boardPanel;
    private JTextField sizeField;
    private JTextField piecesField;
    private List<Integer> selectedPieces = new ArrayList<>();
    private JLabel statusLabel;
    // Track the count of each piece type for UI updates
    private Map<Integer, Integer> pieceCount = new HashMap<>();
    // Keep references to all piece panels
    private Map<Integer, JPanel> piecePanels = new HashMap<>();

    private static final Color[] COLORS = {
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE,
            Color.MAGENTA, Color.CYAN, Color.PINK, Color.LIGHT_GRAY, Color.GRAY, Color.BLACK
    };

    public PackingPuzzleGUI(int[][][] pieceDictionary) {
        this.pieceDictionary = pieceDictionary;
        this.board = new int[0][0]; // Initially empty board
        setTitle("Packing Puzzle");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create main panel for board visualization
        boardPanel = new BoardPanel();
        boardPanel.setBackground(Color.WHITE);
        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        boardContainer.add(boardPanel, BorderLayout.CENTER);
        add(boardContainer, BorderLayout.CENTER);

        // Create side panel for displaying pieces
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(0, 2, 10, 10)); // Grid layout with 2 columns and gaps
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setPreferredSize(new Dimension(300, getHeight()));

        // Create piece panels with clickable behavior
        for (int i = 0; i < pieceDictionary.length; i++) {
            int pieceNumber = i + 1;
            int[][] pieceShape = pieceDictionary[i];

            // Initialize piece count
            pieceCount.put(pieceNumber, 0);

            JPanel pieceContainer = createPiecePanel(pieceShape, pieceNumber);
            piecePanels.put(pieceNumber, pieceContainer);

            // Add click listener for piece selection
            pieceContainer.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Left click to add, right click to remove
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        // Add the piece to selection
                        selectedPieces.add(pieceNumber);
                        pieceCount.put(pieceNumber, pieceCount.get(pieceNumber) + 1);
                        updatePieceDisplay(pieceNumber);
                    } else if (SwingUtilities.isRightMouseButton(e) && pieceCount.get(pieceNumber) > 0) {
                        // Remove one occurrence of the piece
                        selectedPieces.remove(Integer.valueOf(pieceNumber));
                        pieceCount.put(pieceNumber, pieceCount.get(pieceNumber) - 1);
                        updatePieceDisplay(pieceNumber);
                    }
                    updatePiecesField();
                }
            });

            sidePanel.add(pieceContainer);
        }

        JScrollPane scrollPane = new JScrollPane(sidePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.EAST);

        // Create control panel at the bottom
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Board size input
        JLabel sizeLabel = new JLabel("Board Size:");
        sizeField = new JTextField(5);

        // Pieces input
        JLabel piecesLabel = new JLabel("Selected Pieces:");
        piecesField = new JTextField(30);
        piecesField.setEditable(false);

        // Status label
        statusLabel = new JLabel("Left-click to add pieces, right-click to remove. Enter board size and click Solve");
        statusLabel.setForeground(Color.BLUE);

        // Solve button
        JButton solveButton = new JButton("Solve Puzzle");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solvePuzzle();
            }
        });

        // Clear button
        JButton clearButton = new JButton("Clear Selection");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSelection();
            }
        });

        inputPanel.add(sizeLabel);
        inputPanel.add(sizeField);
        inputPanel.add(piecesLabel);
        inputPanel.add(piecesField);
        inputPanel.add(solveButton);
        inputPanel.add(clearButton);

        controlPanel.add(inputPanel, BorderLayout.NORTH);
        controlPanel.add(statusLabel, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void updatePieceDisplay(int pieceNumber) {
        JPanel container = piecePanels.get(pieceNumber);
        if (container != null) {
            // Find the label to update
            for (Component c : container.getComponents()) {
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    int count = pieceCount.get(pieceNumber);
                    label.setText(pieceNumber + (count > 0 ? " (" + count + ")" : ""));
                    break;
                }
            }

            // Update border based on selection status
            if (pieceCount.get(pieceNumber) > 0) {
                container.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            } else {
                container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
        }
    }

    private void updatePiecesField() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedPieces.size(); i++) {
            sb.append(selectedPieces.get(i));
            if (i < selectedPieces.size() - 1) {
                sb.append(", ");
            }
        }
        piecesField.setText(sb.toString());
    }

    private void clearSelection() {
        selectedPieces.clear();
        piecesField.setText("");

        // Reset all piece counts and borders
        for (int pieceNumber : pieceCount.keySet()) {
            pieceCount.put(pieceNumber, 0);
            updatePieceDisplay(pieceNumber);
        }

        // Clear the board
        board = new int[0][0];
        boardPanel.repaint();
        statusLabel.setText("Left-click to add pieces, right-click to remove. Enter board size and click Solve");
    }

    private void solvePuzzle() {
        try {
            // Get board size
            int size = Integer.parseInt(sizeField.getText().trim());
            if (size <= 0) {
                statusLabel.setText("Board size must be a positive number");
                return;
            }

            // Check if pieces are selected
            if (selectedPieces.isEmpty()) {
                statusLabel.setText("Please select at least one piece first");
                return;
            }

            // Convert selected pieces to array
            int[] pieces = new int[selectedPieces.size()];
            for (int i = 0; i < selectedPieces.size(); i++) {
                pieces[i] = selectedPieces.get(i);
            }

            statusLabel.setText("Solving puzzle...");

            // Solve in background thread to keep UI responsive
            new SwingWorker<int[][], Void>() {
                @Override
                protected int[][] doInBackground() throws Exception {
                    return PackingPuzzle.packingPuzzle(pieces, size);
                }

                @Override
                protected void done() {
                    try {
                        board = get();
                        if (board != null && board.length > 0) {
                            // Check if we have a solution by looking for any non-zero value
                            boolean hasSolution = false;
                            for (int i = 0; i < board.length; i++) {
                                for (int j = 0; j < board[i].length; j++) {
                                    if (board[i][j] > 0) {
                                        hasSolution = true;
                                        break;
                                    }
                                }
                                if (hasSolution)
                                    break;
                            }

                            if (hasSolution) {
                                statusLabel.setText("Solution found!");
                            } else {
                                statusLabel.setText("No solution found for the selected pieces and board size");
                            }
                        } else {
                            statusLabel.setText("No solution found for the selected pieces and board size");
                        }
                        boardPanel.repaint();
                    } catch (Exception e) {
                        statusLabel.setText("Error: " + e.getMessage());
                    }
                }
            }.execute();

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid board size. Please enter a valid number.");
        }
    }

    private JPanel createPiecePanel(int[][] pieceShape, int pieceNumber) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Calculate the maximum dimensions of the piece
                int maxRow = 0, maxCol = 0;
                for (int[] point : pieceShape) {
                    maxRow = Math.max(maxRow, point[0]);
                    maxCol = Math.max(maxCol, point[1]);
                }

                // Calculate cell size based on panel dimensions and piece size
                int cellSize = Math.min(getWidth() / (maxCol + 2), getHeight() / (maxRow + 2));
                cellSize = Math.min(cellSize, 25); // Cap max cell size

                // Offset for centering the piece
                int offsetX = (getWidth() - (maxCol + 1) * cellSize) / 2;
                int offsetY = (getHeight() - (maxRow + 1) * cellSize) / 2;

                // Draw the piece
                g.setColor(COLORS[pieceNumber - 1]);
                for (int[] point : pieceShape) {
                    int x = offsetX + point[1] * cellSize;
                    int y = offsetY + point[0] * cellSize;
                    g.fillRect(x, y, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, cellSize, cellSize);
                    g.setColor(COLORS[pieceNumber - 1]);
                }
            }
        };

        panel.setPreferredSize(new Dimension(100, 100));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(String.valueOf(pieceNumber));
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setHorizontalAlignment(JLabel.CENTER);

        JPanel container = new JPanel(new BorderLayout());
        container.add(panel, BorderLayout.CENTER);
        container.add(label, BorderLayout.SOUTH);
        container.setBackground(Color.WHITE);

        return container;
    }

    private class BoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (board.length == 0) {
                g.setColor(Color.GRAY);
                g.setFont(new Font("Arial", Font.BOLD, 18));
                FontMetrics fm = g.getFontMetrics();
                String message = "Solution will appear here";
                int textWidth = fm.stringWidth(message);
                int textHeight = fm.getHeight();
                g.drawString(message, (getWidth() - textWidth) / 2, (getHeight() - textHeight) / 2 + fm.getAscent());
                return;
            }

            int rows = board.length;
            int cols = board[0].length;
            int cellSize = Math.min(getWidth() / (cols + 2), getHeight() / (rows + 2));

            // Center the board in the panel
            int offsetX = (getWidth() - cols * cellSize) / 2;
            int offsetY = (getHeight() - rows * cellSize) / 2;

            Map<Integer, Color> pieceColors = new HashMap<>();
            for (int i = 0; i < COLORS.length; i++) {
                pieceColors.put(i + 1, COLORS[i]);
            }

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    int pieceType = board[row][col];
                    g.setColor(Color.WHITE);
                    g.fillRect(offsetX + col * cellSize, offsetY + row * cellSize, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(offsetX + col * cellSize, offsetY + row * cellSize, cellSize, cellSize);

                    if (pieceType > 0) {
                        g.setColor(pieceColors.get(pieceType));
                        g.fillRect(offsetX + col * cellSize, offsetY + row * cellSize, cellSize, cellSize);
                        g.setColor(Color.BLACK);
                        g.drawRect(offsetX + col * cellSize, offsetY + row * cellSize, cellSize, cellSize);
                    }
                }
            }

            // Draw a thicker border around the entire board
            g.setColor(Color.BLACK);
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.drawRect(offsetX, offsetY, cols * cellSize, rows * cellSize);
        }
    }

    public static void displayGUI(int[][][] pieceDictionary) {
        SwingUtilities.invokeLater(() -> {
            PackingPuzzleGUI gui = new PackingPuzzleGUI(pieceDictionary);
            gui.setVisible(true);
        });
    }
}
package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.io.File;

public class Tools {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void wait(int time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static String centerString(String s, int size) {
        if (s.length() >= size) {
            return s;
        } else {
            int padding = (size - s.length()) / 2;
            String centeredString = String.format("%" + (padding + s.length()) + "s", s);
            centeredString = String.format("%-" + size + "s", centeredString);
            return centeredString;
        }
    }

    public static boolean askYN() {
        String r = "";
        boolean b = false;
    
        do {
                try {
                    r = reader.readLine().toLowerCase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            b = r.equals("o") || r.equals("n");
            if (!b) {
                System.err.print(ConsoleColors.RED_BRIGHT);
                System.out.println("Erreur, veuillez réessayer.");
                System.out.println(ConsoleColors.RESET);
            }
        } while (!b);
        return r.charAt(0) == 'o';
    }
    
    public static void printSperator(int size) {
        for (int i = 0; i < size; i++) {
            System.out.print("─");
        }
        System.out.println();
    }

    public static int cursor(String[][] options, MyFunction myFunction) {
        int cursorRow = 0;
        int cursorCol = 0;
        char userInput = ' ';
        String input = "";
        while (userInput != 'e') {
            if(myFunction != null) {
                myFunction.apply();
            }
            System.err.print(ConsoleColors.YELLOW_BRIGHT);
            System.out.println("Choisir action avec les contrôles : \"zqsd\" puis valider avec : \"e\"");
            System.out.println(ConsoleColors.RESET);
            for (int i = 0; i < options.length; i++) {
                for (int j = 0; j < options[i].length; j++) {
                    if (i == cursorRow && j == cursorCol) {
                        System.out.print("⯈");
                    } else {
                        System.out.print(" ");
                    }
                    System.out.print(options[i][j] + "  ");
                }
                System.out.println();
            }
            do {
                try {
                    input = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (input.length() == 0);
            userInput = input.charAt(0);
            clearScreen();
            if(input.isEmpty()){
                return cursorRow + cursorCol;
            }
            switch (userInput) {
                case 's':
                    cursorRow = (cursorRow + 1) % options.length;
                    break;
                case 'z':
                    cursorRow = (cursorRow - 1 + options.length) % options.length;
                    break;
                case 'd':
                    cursorCol = (cursorCol + 1) % options[cursorRow].length;
                    break;
                case 'q':
                    cursorCol = (cursorCol - 1 + options[cursorRow].length) % options[cursorRow].length;
                    break;
                case 'e':
                    return cursorRow + cursorCol;                    
                default:
                    System.err.print(ConsoleColors.RED_BRIGHT);
                    System.out.println("Entrée non reconnue. Veuillez réessayer.");
                    System.out.println(ConsoleColors.RESET);
                    break;
            }
        }
        return -1;
    }

    public static void clearScreen() {
        try {
            ProcessBuilder pb = new ProcessBuilder("clear"); //use clear command from bash instead
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public static void printFile(File f, String color, boolean center, int screenSize) {
        if (color.length() != 0) {
            System.out.println(color);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            if (center) {
                while ((line = reader.readLine()) != null) {
                    System.out.println(centerString(line, screenSize));
                }
            } else {
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(ConsoleColors.RESET); //Color Reset
    }
}
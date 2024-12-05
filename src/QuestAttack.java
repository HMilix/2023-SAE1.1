import utils.CSVFile;
import utils.ConsoleColors;
import utils.MyFunction;
import utils.Tools;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

class QuestAttack {
    static CSVFile attacks = new CSVFile("ressources/attaques.csv");
    static CSVFile questAttackSave = new CSVFile("ressources/QuestAttackSave.csv");
    static CSVFile items = new CSVFile("ressources/equipement.csv");
    static CSVFile inventory = new CSVFile("ressources/inventaire.csv");
    final static File LOGO = new File("ressources/logo.txt");
    final static File COMBAT = new File("ressources/combat.txt");
    final static int SCREENSIZE = 190;
    final static Item[] ITEMSLIST = loadItems();
     final static ArrayList<Questions>[] QUESTIONSLIST = loadQuestions();

    static Entity h = new Entity(1, 0, 200, 30, 30, 20, 20, 10, 4,
            new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            new boolean[] { false, false, false, false, false, false, false, false, false },
            new boolean[] { false, false, false, false, false, false, false, false, false },
            true);
    static Entity m = new Entity(20, 100, 20, 20, 5, 1);
    static boolean loaded = false;
    static boolean end = false;
    static boolean fighting = false;
    static int boost = 0;
    
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        Tools.clearScreen();
        sizeCheck();

        Tools.clearScreen();
        Tools.printFile(LOGO, ConsoleColors.CYAN_BRIGHT, true, SCREENSIZE);
        Tools.wait(750);

        while (!loaded && !end) {
            saveMenu();
        }
        while (!end) {
            mainMenu();
        }
        System.out.println("Fin de partie et fin du programme.");
    }

// ──────────────────────────────────────────── { GAME } ────────────────────────────────────────────\\

    private static void mainMenu() {
        Tools.clearScreen();
        MyFunction myFunction = () -> Tools.printFile(LOGO, "\033[1;96m", true, SCREENSIZE);
        switch (Tools.cursor(new String[][] { { "Combat" },
                { "Inventaire" },
                { "Boutique" },
                { "Menu sauvegardes" }},
                myFunction)) {
            case 0:
                int difficulty;
                if (h.niveau >= 1 && h.niveau <= 10) {
                    difficulty = 1;
                } else if (h.niveau >= 11 && h.niveau <= 20) {
                    difficulty = 2;
                } else if (h.niveau >= 21) {
                    difficulty = 3;
                } else {
                    difficulty = 1;
                }
                m.monsterDifficulty(difficulty);
                fight(difficulty);
                break;
            case 1:
                inventory();
                mainMenu();
                break;
            case 2:
                shop();
                mainMenu();
                break;
            case 3:
                saveMenu();
                break;
            default:
                mainMenu();
                break;
        }
    }

    private static void shop() {
        int cursorIndex = 0;
        char userInput = ' ';
        String input = "";
        boolean ended = false;
        while (!ended) {
            Tools.printSperator(SCREENSIZE);
            System.out.print(ConsoleColors.GREEN_BRIGHT);
            System.out.println(Tools.centerString("Boutique", SCREENSIZE));
            System.out.print(ConsoleColors.RESET);
            System.out.println("              Nom de l'équipement              TYPE     Valeur     Prix   Est possédé                                            "
                            + h.or + " or");
            Tools.printSperator(SCREENSIZE);
            System.out.print(ConsoleColors.RESET);
            System.out.println("┌──┬────────────────────────────────────────┬─────────┬─────────┬─────────┬─────────┐");
            for (int cpt = 1; cpt < ITEMSLIST.length; cpt++) {
                System.out.print("│ ");
                System.out.print(ConsoleColors.GREEN_BRIGHT);
                if (cpt - 1 == cursorIndex) {
                    System.out.print("⯈");
                } else {
                    System.out.print(" ");
                }
                System.out.print(ConsoleColors.RESET);
                System.out.print("│");
                System.out.print(ConsoleColors.CYAN);
                System.out.print(Tools.centerString(ITEMSLIST[cpt].getName(), 40));
                System.out.print(ConsoleColors.RESET);
                System.out.print("│");
                String type = "";
                System.out.print(ConsoleColors.BLUE);
                switch (ITEMSLIST[cpt].getType()) {
                    case ARME:
                        type = "ARME";
                        break;
                    case ARMURE:
                        type = "ARMURE";
                        break;
                    case ACCESSOIRE:
                        type = "ACCESSOI.";
                        break;
                    case BUFF:
                        type = "BOOST";
                        break;
                    case POTION:
                        type = "POTION";
                        break;
                    default:
                        break;
                }
                System.out.print(Tools.centerString(type, 9));
                System.out.print(ConsoleColors.RESET);
                System.out.print("│");
                System.out.print(ConsoleColors.RED_BRIGHT);
                System.out.print(Tools.centerString(ITEMSLIST[cpt].getValue() + "", 9));
                System.out.print(ConsoleColors.RESET);
                System.out.print("│");
                System.out.print(ConsoleColors.YELLOW);
                System.out.print(Tools.centerString(ITEMSLIST[cpt].getPrice() + "", 9));
                System.out.print(ConsoleColors.RESET);
                System.out.print("│");
                if (ITEMSLIST[cpt].isOwned(h)) {
                    System.out.print(ConsoleColors.GREEN_BRIGHT);
                    System.out.print(Tools.centerString("⬤", 9));
                } else {
                    System.out.print(Tools.centerString(" ", 9));
                }
                System.out.print(ConsoleColors.RESET);
                System.out.print("│");
                System.out.println();
                if (cpt < ITEMSLIST.length - 1) {
                    System.out.println("├──┼────────────────────────────────────────┼─────────┼─────────┼─────────┼─────────┤");
                }
            }
            System.out.println("└──┴────────────────────────────────────────┴─────────┴─────────┴─────────┴─────────┘");
            System.out.print(ConsoleColors.RESET);
            System.out.println(" ⬤ : L'objet est actuellement possédé.");
            System.err.print(ConsoleColors.YELLOW_BRIGHT);
            System.out.println("Choisissez votre objet avec les contrôles : \"zqsd\" puis validez avec : \"e\", finissez avec : \"a\".");
            System.out.print(ConsoleColors.RESET);
            do {
                try {
                    input = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (input.length() == 0);
            userInput = input.charAt(0);
            Tools.clearScreen();
            switch (userInput) {
                case 's':
                    cursorIndex = (cursorIndex + 1) % (ITEMSLIST.length - 1);
                    break;
                case 'z':
                    cursorIndex = (cursorIndex - 1 + ITEMSLIST.length - 1) % (ITEMSLIST.length - 1);
                    break;
                case 'e':
                    if (ITEMSLIST[cursorIndex + 1].isOwned(h)) {
                        System.out.println("Uh oh ! Il semblerait que vous ayez déjà l'objet !");
                    } else {
                        if (h.enoughGold(ITEMSLIST[cursorIndex + 1])) {
                            if (h.addItem(ITEMSLIST[cursorIndex + 1].getId())) {
                                h.removeGold(ITEMSLIST[cursorIndex + 1]);
                                System.out.println("Vous avez acheté : \"" + ITEMSLIST[cursorIndex + 1].getName() + "\" !");
                            } else {
                                System.out.println(
                                        "Uh oh ! Il semblerait que vous n'avez plus de place dans votre inventaire !");
                            }
                        } else {
                            System.out.println("Uh oh ! Il semblerait que vous n'avez pas assez d'argent !");
                        }
                    }
                    break;
                case 'a':
                    ended = true;
                    mainMenu();
                    break;
                default:
                    System.out.println("Entrée non reconnue. Veuillez réessayer.");
            }
        }
    }

    public static void inventory() {
        int cursorIndex = 0;
        char userInput = ' ';
        String input = "";
        boolean ended = false;
        while (!ended) {
            Tools.printSperator(SCREENSIZE);
            System.out.print(ConsoleColors.GREEN_BRIGHT);
            System.out.println(Tools.centerString("Inventaire", SCREENSIZE));
            System.out.print(ConsoleColors.RESET);
            Tools.printSperator(SCREENSIZE);
            System.out.println("┌──┬───┬────────────────────────────────────────┬──┬───┬────────────────────────────────────────┬──┬───┬─────────────────────────────────────────┐");
            for (int cpt = 0; cpt < 9; cpt++) {
                System.out.print("│ ");
                System.out.print(ConsoleColors.GREEN_BRIGHT);
                if (cpt == cursorIndex) {
                    System.out.print("⯈");
                } else {
                    System.out.print(" ");
                }
                System.out.print(ConsoleColors.RESET);
                System.out.print("│");
                System.out.print(ConsoleColors.GREEN_BRIGHT);
                if (h.objetsEquipe[cpt]) {
                    System.out.print(" ⬤");
                } else {
                    System.out.print("  ");
                }
                System.out.print(ConsoleColors.RESET);
                System.out.print(" │");
                System.out.print(ConsoleColors.CYAN_BRIGHT);
                if (ITEMSLIST[h.objets[cpt]].getType() == Type.VIDE) {
                    System.out.print(ConsoleColors.CYAN);
                }
                System.out.print(Tools.centerString(ITEMSLIST[h.objets[cpt]].getName(), 40));
                System.out.print(ConsoleColors.RESET);
                if (cpt != 8 && (cpt % 3) - 2 == 0) {
                    System.out.println(" │\n├──┼───┼────────────────────────────────────────┼──┼───┼────────────────────────────────────────┼──┼───┼─────────────────────────────────────────┤");
                } else if (cpt == 8) {
                    System.out.println(" │");
                }
            }
            System.out.println("└──┴───┴────────────────────────────────────────┴──┴───┴────────────────────────────────────────┴──┴───┴─────────────────────────────────────────┘");
            System.out.println(" ⬤ : L'objet est actuellement équipé.");
            printDesc(ITEMSLIST[h.objets[cursorIndex]]);
            printStats(h);
            System.err.print(ConsoleColors.YELLOW_BRIGHT);
            System.out.println("Choisissez votre objet avec les contrôles : \"zqsd\" puis validez avec : \"e\", finissez avec : \"a\" ou jetez un objet avec : \"r\".");
            System.out.print(ConsoleColors.RESET);
            do {
                try {
                    input = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (input.length() == 0);
            userInput = input.charAt(0);
            Tools.clearScreen();

            switch (userInput) {
                case 's':
                    cursorIndex = (cursorIndex + 3) % 9;
                    break;
                case 'z':
                    cursorIndex = (cursorIndex - 3 + 9) % 9;
                    break;
                case 'd':
                    cursorIndex = (cursorIndex + 1) % 9;
                    break;
                case 'q':
                    cursorIndex = (cursorIndex - 1 + 9) % 9;
                    break;
                case 'e':
                    if(ITEMSLIST[h.objets[cursorIndex]].getType() != Type.VIDE) {
                        if (ITEMSLIST[h.objets[cursorIndex]].getType() == Type.POTION) {
                            h.usePotion(h, ITEMSLIST[h.objets[cursorIndex]]);
                        } else {
                            h.toggleItem(ITEMSLIST[h.objets[cursorIndex]], ITEMSLIST);
                        }
                        h.refreshStats(ITEMSLIST);
                    }
                    break;
                case 'r':
                    if (ITEMSLIST[h.objets[cursorIndex]].type != Type.VIDE) {
                        System.out.print(ConsoleColors.RED_BRIGHT);
                        System.out.println(Tools.centerString(
                                "Voulez-vous vraiment jeter \"" + ITEMSLIST[h.objets[cursorIndex]].name + "\" ? (o/n)",
                                SCREENSIZE));
                        if (Tools.askYN()) {
                            h.removeItem(ITEMSLIST[h.objets[cursorIndex]]);
                            h.refreshStats(ITEMSLIST);
                        }
                        System.out.print(ConsoleColors.RESET);
                        Tools.clearScreen();
                    } else {
                        System.out.println("Vous ne pouvez pas jeter un objet inéxistant.");
                    }
                    break;
                case 'a':
                    ended = true;
                    if(!fighting) {
                        mainMenu();
                    }
                    break;
                default:
                    System.out.println("Entrée non reconnue. Veuillez réessayer.");
            }
        }
    }

    private static void fight(int difficulty) {
        fighting = true;
        Random random = new Random();
        printFight(difficulty);
        System.out.println();
        boolean fincombat = false;
        boolean fuite = false;
        MyFunction myFunction = () -> printFight(0);
        while (!fincombat && !fuite) {
            if(boost <= 0) {
                h.refreshStats(ITEMSLIST);
            }
            switch (Tools.cursor(new String[][] { { "Attaque" }, { "Défense" }, {"Stats"} ,{ "Objet" }, { "Fuite" } }, myFunction)) {
                case 0:
                    if(boost > 0) {
                        boost--;
                    }
                    printFight(0);
                    if (attack(difficulty)) {
                        
                        System.out.print(ConsoleColors.GREEN_BRIGHT);
                        System.out.println("C'est la bonne réponse, vous attaquez.");
                        System.out.print(ConsoleColors.RESET);
                        printFight(1000);
                        int att = h.attack(m);
                        m.setPv(m.getPv() - att);
                        System.out.println("Vous avez infligé : " + att + " points de dégâts.");
                        printFight(1500);
                    } else {
                        System.out.print(ConsoleColors.RED_BRIGHT);
                        System.out.println("C'est une mauvaise réponse, vous ratez votre attaque.");
                        System.out.print(ConsoleColors.RESET);
                        printFight(1000);
                    }
                    printFight(250);
                    if (m.pv > 0) {
                        System.out.print(ConsoleColors.YELLOW);
                        System.out.println("C'est le tour de l'adversaire.");
                        System.out.print(ConsoleColors.RESET);
                        Tools.wait(750);
                        if (random.nextDouble() > 0.15) {
                            int att = m.attack(h);
                            h.setPv(h.getPv() - att);
                            System.out.println("Il attaque et vous inflige : " + att + " points de dégâts.");
                            printFight(1500);
                        } else {
                            System.out.println("Il rate son attaque.");
                            printFight(1500);
                        }
                    }
                    break;
                case 1:
                    printFight(0);
                    if (h.getMana() < 5) {
                        System.out.print(ConsoleColors.RED_BRIGHT);
                        System.out.println("Vous n'avez pas assez de Mana !");
                        System.out.print(ConsoleColors.RESET);
                        Tools.wait(1750);
                        break;
                    } else if(!(boost <= 0)) {
                        System.out.print(ConsoleColors.RED_BRIGHT);
                        System.out.println("Vous avez déjà un boost en cours !");
                        System.out.print(ConsoleColors.RESET);
                        Tools.wait(1750);
                        break;
                    }
                    h.setMana(h.getMana() - 5);
                    boost = 3;
                    h.setDefense(h.getDefense() + 5);
                    System.out.print(ConsoleColors.GREEN_BRIGHT);
                    System.out.println("Vôtre défense est boosté pour 3 tours !");
                    System.out.println(ConsoleColors.RESET);
                    printFight(2000);
                    if (m.pv > 0) {
                        System.out.print(ConsoleColors.YELLOW);
                        System.out.println("C'est le tour de l'adversaire.");
                        System.out.print(ConsoleColors.RESET);
                        Tools.wait(750);
                        if (random.nextDouble() > 0.15) {
                            int att = m.attack(h);
                            h.setPv(h.getPv() - att);
                            System.out.println("Il attaque et vous inflige : " + att + " points de dégâts.");
                            printFight(1500);
                        } else {
                            System.out.println("Il rate son attaque.");
                            printFight(1500);
                        }
                    }
                    break;
                case 2:
                printFight(0);
                    MyFunction printHP = () -> printHP(m, 30);
                    MyFunction printMana = () -> printMana(m, 30);
                    m.printStats(printHP, printMana);
                    try {
                        reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    inventory();
                    break;
                case 4:
                    if (h.getMana() >= 15) {
                        fuite = true;
                        h.setMana(h.getMana() - 15);
                        mainMenu();
                    } else {
                        System.out.print(ConsoleColors.RED_BRIGHT);
                        System.out.println("Vous n'avez pas assez de Mana pour fuir !");
                        System.out.print(ConsoleColors.RESET);
                        Tools.wait(1750);
                    }
                    break;
                default:
                    break;
            }
            fincombat = h.battleEnded(m);
        }
        if (h.winner() && !fuite) {
            System.out.print(ConsoleColors.GREEN_BRIGHT);
            System.out.println("Vous remportez le combat et " + h.giveGold(m) + " d'or et " + h.giveXP(m) + " d'xp.");
            if (h.giveLv()) {
                System.out.println("Vous avez gagné un niveau ! Vous êtes niveau : " + h.getNiveau());
            }
            System.out.println("Niveau actuel : " + h.xp + "/" + h.calcMaxXP());
            System.out.println("(Appuyez sur n'importe quelle touche pour continuer.)");
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print(ConsoleColors.RESET);
        } else if (h.pv <= 0) {
            System.out.print(ConsoleColors.RED_BRIGHT);
            System.out.println("Vous êtes mort.");
            System.out.println("GAME OVER");
            System.out.print(ConsoleColors.RESET);
            System.out.println("(Appuyez sur n'importe quelle touche pour continuer.)");
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            h.regen();
        }

        resetQuestionsAsked();
        fighting = false;
        mainMenu();
    }

    private static boolean attack(int difficulty) {
        Random random = new Random();
        System.out.println("Répondez à la question correctement pour réussir votre attaque.");
        int rand = random.nextInt(QUESTIONSLIST[difficulty - 1].size());
        System.out.print(ConsoleColors.YELLOW);
        do {
            rand = random.nextInt(QUESTIONSLIST[difficulty - 1].size());
        } while (!QUESTIONSLIST[difficulty - 1].get(rand).ask());
        String answer = "";
        System.out.print(ConsoleColors.BLUE_BOLD_BRIGHT);
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(ConsoleColors.RESET);
        return QUESTIONSLIST[difficulty - 1].get(rand).valid(answer);
    }

// ──────────────────────────────────────────── { PRINTABLES } ────────────────────────────────────────────\\

    private static void printFight(int delay) {
        Tools.wait(delay);
        Tools.clearScreen();
        Tools.printSperator(SCREENSIZE);
        System.out.print("            PV Héros = ");
        printHP(h, 30);
        System.out.print("                                    " + "PV Monstre = ");
        printHP(m, 30);
        System.out.println();
        Tools.printFile(COMBAT, ConsoleColors.RED_BRIGHT, false, 0);
        Tools.printSperator(SCREENSIZE);
    }

    private static void printStats(Entity e) {
        MyFunction printHP = () -> printHP(e, 30);
        MyFunction printMana = () -> printMana(e, 30);
        Tools.printSperator(SCREENSIZE);
        e.printStats(printHP, printMana);
        Tools.printSperator(SCREENSIZE);
    }

    private static void printHP(Entity e, int length) {
        int pvBarLength = Math.max(0, (int) ((double) e.pv / e.pvmax * length));
        StringBuilder pvBar = new StringBuilder("[");
        for (int i = 0; i < length; i++) {
            if (i < pvBarLength) {
                pvBar.append(ConsoleColors.GREEN_BRIGHT);
                pvBar.append("█");
            } else {
                pvBar.append(ConsoleColors.RED_BRIGHT);
                pvBar.append("░");
            }
        }
        pvBar.append(ConsoleColors.RESET);
        pvBar.append("]");
        int a = (e.pv < 0 ? 0 : e.pv);
        String pv = String.format("%02d", a);
        pvBar.insert(0, "[" + pv + "/" + e.pvmax + "]");
        System.out.print(pvBar.toString());
    }

    private static void printMana(Entity e, int length) {
        int pvBarLength = Math.max(0, (int) ((double) e.getMana() / e.getManamax() * length));
        StringBuilder pvBar = new StringBuilder("[");
        for (int i = 0; i < length; i++) {
            if (i < pvBarLength) {
                pvBar.append(ConsoleColors.BLUE);
                pvBar.append("█");
            } else {
                pvBar.append(ConsoleColors.RED_BRIGHT);
                pvBar.append("░");
            }
        }
        pvBar.append(ConsoleColors.RESET);
        pvBar.append("]");
        int a = (e.getMana() < 0 ? 0 : e.getMana());
        String mana = String.format("%02d", a);
        pvBar.insert(0, "[" + mana + "/" + e.getManamax() + "]");
        System.out.print(pvBar.toString());
    }

    private static void printDesc(Item i) {
        Tools.printSperator(SCREENSIZE);
        i.printStats();
        Tools.printSperator(SCREENSIZE);
    }

// ──────────────────────────────────────────── { SAVES / CSV } ────────────────────────────────────────────\\

    private static void saveMenu() {
        Tools.clearScreen();
        Tools.printFile(LOGO, ConsoleColors.CYAN_BRIGHT, true, SCREENSIZE);
        Tools.printSperator(SCREENSIZE);
        System.out.println(Tools.centerString("Menu des sauvgardes", SCREENSIZE));
        System.out.println(Tools.centerString("Sauvegardes :", SCREENSIZE));
        for (int i = 0; i < 3; i++) {
            if (Integer.parseInt(questAttackSave.getCell(i, 0)) != 0) {
                System.out.println(
                        Tools.centerString((i + 1) + " : Niveau = " + questAttackSave.getCell(i, 0), SCREENSIZE));
            } else {
                System.out.println(Tools.centerString((i + 1) + " : Inéxistante", SCREENSIZE));
            }
        }
        System.out.println();
        System.out.println(Tools.centerString("q : Quitter.", SCREENSIZE));
        System.out.println();
        System.out.println(Tools.centerString("Emplacement de la sauvegarde ? (1/2/3)", SCREENSIZE));
        String t = "";
        char c;
        int choice = 0;

        do {
            do {
                try {
                    t = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (t.equals(""));
            c = t.charAt(0);
            choice = Character.getNumericValue(c);
            if (c == 'q') {
                Tools.clearScreen();
                end = true;
            } else if (!(choice >= 1 && choice <= 3)) {
                System.out.println(Tools.centerString("Emplacement de la sauvegarde invalide.", SCREENSIZE));
            } else {
                saveChoice(choice);
            }
        } while (!end);
    }

    private static void saveChoice(int idx) {
        boolean saveExist = Integer.parseInt(questAttackSave.getCell(idx - 1, 0)) == 0;
        System.out.println("Sauvegarde numéro : " + idx);
        Tools.clearScreen();
        MyFunction myFunction = () -> Tools.printFile(LOGO, ConsoleColors.CYAN_BRIGHT, true, SCREENSIZE);
        switch (Tools.cursor(new String[][] {
                        { saveExist ? "𝖢̶𝗁̶𝖺̶𝗋̶𝗀̶𝖾̶𝗋̶ 𝗅̶𝖺̶ 𝗌̶𝖺̶𝗎̶𝗏̶𝖾̶𝗀̶𝖺̶𝗋̶𝖽̶𝖾̶"
                                : "Charger la sauvegarde" },
                        { saveExist ? "𝖲̶𝖺̶𝗎̶𝗏̶𝖾̶𝗀̶𝖺̶𝗋̶𝖽̶𝖾̶𝗋̶" : "Sauvegarder" },
                        { "Effacer la sauvegarde" },
                        { "Créer sauvegarde" },
                        { "Retour" } },
                        myFunction)) {
            case 0:
                if (Integer.parseInt(questAttackSave.getCell(idx - 1, 0)) != 0) {
                    load(idx);
                } else {
                    System.err.print(ConsoleColors.RED);
                    System.out.println("Vous ne pouvez pas charger une sauvegarde inéxistente.");
                    System.out.print(ConsoleColors.RESET);
                    Tools.wait(1500);
                    saveChoice(idx);
                }
                break;
            case 1:
                if (Integer.parseInt(questAttackSave.getCell(idx - 1, 0)) != 0) {
                    if (loaded) {
                        save(idx);
                    } else {
                        System.err.print(ConsoleColors.RED);
                        System.out.println("Vous ne pouvez pas sauvegarder une sauvegarde non chargée.");
                        System.out.print(ConsoleColors.RESET);
                    }
                } else {
                    System.err.print(ConsoleColors.RED);
                    System.out.println("Vous ne pouvez pas sauvegarder une sauvegarde inéxistente.");
                    System.out.print(ConsoleColors.RESET);
                }
                Tools.wait(1500);
                saveChoice(idx);
                break;
            case 2:
                saveErase(idx);
                break;
            case 3:
                saveCreate(idx);
                break;
            case 4:
                saveMenu();
                break;
            default:
                saveMenu();
                break;
        }
    }

    private static void load(int idx) {
        System.out.print(ConsoleColors.YELLOW);
        System.out.println(Tools.centerString("Voulez-vous vraiment charger cette sauvegarde ? (o/n)", SCREENSIZE));
        System.out.print(ConsoleColors.RESET);
        if (Tools.askYN()) {
            h.niveau = Integer.parseInt(questAttackSave.getCell(idx - 1, 0));
            h.xp = Integer.parseInt(questAttackSave.getCell(idx - 1, 1));
            h.or = Integer.parseInt(questAttackSave.getCell(idx - 1, 2));
            h.pvmax = Integer.parseInt(questAttackSave.getCell(idx - 1, 3));
            h.pv = Integer.parseInt(questAttackSave.getCell(idx - 1, 4));
            h.mana = Integer.parseInt(questAttackSave.getCell(idx - 1, 5));
            h.manamax = Integer.parseInt(questAttackSave.getCell(idx - 1, 6));
            h.attaque = Integer.parseInt(questAttackSave.getCell(idx - 1, 7));
            h.defense = Integer.parseInt(questAttackSave.getCell(idx - 1, 8));
            for (int cpt = 0; cpt < 9; cpt++) {
                h.objets[cpt] = Integer.parseInt(inventory.getCell(idx - 1, cpt));
                if (h.objets[cpt] != 0) {
                    h.objetsExiste[cpt] = true;
                }
                if (Integer.parseInt(inventory.getCell(idx - 1, cpt + 9)) == 1) {
                    h.objetsEquipe[cpt] = true;
                }
            }
            loaded = true;
            mainMenu();
        } else {
            saveMenu();
        }
    }

    private static void save(int idx) {
        System.out.print(ConsoleColors.YELLOW);
        System.out.println(Tools.centerString("Voulez-vous sauvgarder ? (o/n)", SCREENSIZE));
        System.out.print(ConsoleColors.RESET);
        if (Tools.askYN()) {
            int column = questAttackSave.columnCount(), lines = questAttackSave.rowCount();
            int column2 = inventory.columnCount(), lines2 = inventory.rowCount();
            String[][] tab = new String[lines][column];
            String[][] tab2 = new String[lines2][column2];

            // Write existing saves
            for (int c = 0; c < column; c++) {
                for (int l = 0; l < lines; l++) {
                    tab[l][c] = questAttackSave.getCell(l, c);
                }
            }
            for (int c = 0; c < column2; c++) {
                for (int l = 0; l < lines2; l++) {
                    tab2[l][c] = inventory.getCell(l, c);
                }
            }

            // Adding Save
            tab[idx - 1][0] = h.niveau + "";
            tab[idx - 1][1] = h.xp + "";
            tab[idx - 1][2] = h.or + "";
            tab[idx - 1][3] = h.pvmax + "";
            tab[idx - 1][4] = h.pv + "";
            tab[idx - 1][5] = h.mana + "";
            tab[idx - 1][6] = h.manamax + "";
            tab[idx - 1][7] = h.attaque + "";
            tab[idx - 1][8] = h.defense + "";
            for (int cpt = 0; cpt < 9; cpt++) {
                tab2[idx - 1][cpt] = h.objets[cpt] + "";
            }
            for (int cpt2 = 9; cpt2 < 18; cpt2++) {
                if (h.objetsEquipe[cpt2 - 9]) {
                    tab2[idx - 1][cpt2] = "1";
                } else {
                    tab2[idx - 1][cpt2] = "0";
                }
            }

            // Save
            try {
                CSVFile.save(tab, "ressources/questAttackSave.csv", ',');
                CSVFile.save(tab2, "ressources/inventaire.csv", ',');
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print(Tools.centerString("Partie Sauvegardée !", SCREENSIZE));
            Tools.wait(1000);
        }
        refreshCSVs();
        saveMenu();
    }

    private static void saveCreate(int idx) {
        Entity h = new Entity(1, 0, 200, 30, 30, 20, 20, 10, 4,
                new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                new boolean[] { false, false, false, false, false, false, false, false, false },
                new boolean[] { false, false, false, false, false, false, false, false, false },
                true);
        System.out.print(ConsoleColors.RED_BRIGHT);
        System.out.println(Tools.centerString("Écraser la sauvegarde existante ? (o/n)", SCREENSIZE));
        System.out.print(ConsoleColors.RESET);
        if (Tools.askYN()) {
            int column = questAttackSave.columnCount(), lines = questAttackSave.rowCount();
            int column2 = inventory.columnCount(), lines2 = inventory.rowCount();
            String[][] tab = new String[lines][column];
            String[][] tab2 = new String[lines2][column2];

            // Write existing saves
            for (int c = 0; c < column; c++) {
                for (int l = 0; l < lines; l++) {
                    tab[l][c] = questAttackSave.getCell(l, c);
                }
            }
            for (int c = 0; c < column2; c++) {
                for (int l = 0; l < lines2; l++) {
                    tab2[l][c] = inventory.getCell(l, c);
                }
            }

            // Create New save
            tab[idx - 1][0] = h.niveau + "";
            tab[idx - 1][1] = h.xp + "";
            tab[idx - 1][2] = h.or + "";
            tab[idx - 1][3] = h.pvmax + "";
            tab[idx - 1][4] = h.pv + "";
            tab[idx - 1][5] = h.mana + "";
            tab[idx - 1][6] = h.manamax + "";
            tab[idx - 1][7] = h.attaque + "";
            tab[idx - 1][8] = h.defense + "";
            for (int cpt = 0; cpt < 18; cpt++) {
                tab2[idx - 1][cpt] = "0";
            }

            // Save
            try {
                CSVFile.save(tab, "ressources/questAttackSave.csv", ',');
                CSVFile.save(tab2, "ressources/inventaire.csv", ',');
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print(ConsoleColors.CYAN_BRIGHT);
            System.out.print(Tools.centerString("Sauvegarde Créée !", SCREENSIZE));
            System.out.print(ConsoleColors.RESET);
            Tools.wait(1000);
        }
        refreshCSVs();
        saveMenu();
    }

    private static void saveErase(int idx) {
        System.out.print(ConsoleColors.RED_BRIGHT);
        System.out.println(Tools.centerString("Voulez-vous vraiment supprimer cette sauvegarde ? (o/n)", SCREENSIZE));
        System.out.print(ConsoleColors.RESET);
        if (Tools.askYN()) {
            int column = questAttackSave.columnCount(), lines = questAttackSave.rowCount();
            int column2 = inventory.columnCount(), lines2 = inventory.rowCount();
            String[][] tab = new String[lines][column];
            String[][] tab2 = new String[lines2][column2];

            // Write existing saves
            for (int c = 0; c < column; c++) {
                for (int l = 0; l < lines; l++) {
                    tab[l][c] = questAttackSave.getCell(l, c);
                }
            }
            for (int c = 0; c < column2; c++) {
                for (int l = 0; l < lines2; l++) {
                    tab2[l][c] = inventory.getCell(l, c);
                }
            }

            // Erase Game Save
            for (int i = 0; i < 9; i++) {
                tab[idx - 1][i] = "0";
            }
            for (int cpt = 0; cpt < 18; cpt++) {
                tab2[idx - 1][cpt] = "0";
            }

            // Save
            try {
                CSVFile.save(tab, "ressources/questAttackSave.csv", ',');
                CSVFile.save(tab2, "ressources/inventaire.csv", ',');
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print(ConsoleColors.CYAN_BRIGHT);
            System.out.print(Tools.centerString("Sauvegarde Supprimée !", SCREENSIZE));
            System.out.print(ConsoleColors.RESET);
            Tools.wait(1000);
        }
        refreshCSVs();
        saveMenu();
    }

    private static void refreshCSVs() {
        attacks = new CSVFile("ressources/attaques.csv");
        questAttackSave = new CSVFile("ressources/questAttackSave.csv");
        items = new CSVFile("ressources/equipement.csv");
        inventory = new CSVFile("ressources/inventaire.csv");
    }

    // ──────────────────────────────────────────── { MISC} ────────────────────────────────────────────\\

    private static void sizeCheck() {
        System.out.println("Réglez votre fenêtre pour que la ligne ci dessous s'affiche entièrement sans saut à la ligne.");
        System.out.print(ConsoleColors.CYAN_BRIGHT);
        Tools.printSperator(SCREENSIZE);
        System.out.print(ConsoleColors.RESET);
        System.out.println("(Recommandé pour écrans 1920x1080 : Taille police = 12, marges = 0)");
        System.out.println("Assurez vous également que les caractères suivant s'affiche correctement (Font conseillé : Casadia Mono) : \"█\" \"▓\" \"▒\" \"░\"");
        System.out.println("Appuyez sur entrer pour continuer.");
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Item[] loadItems() {
        Item[] result = new Item[items.rowCount()];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Item(Integer.parseInt(items.getCell(i, 0)),
                    Type.VIDE,
                    items.getCell(i, 2),
                    Integer.parseInt(items.getCell(i, 3)),
                    Integer.parseInt(items.getCell(i, 4)));
            switch (Integer.parseInt(items.getCell(i, 1))) {
                case 1:
                    result[i].type = Type.ARME;
                    break;
                case 2:
                    result[i].type = Type.ARMURE;
                    break;
                case 3:
                    result[i].type = Type.ACCESSOIRE;
                    break;
                case 4:
                    result[i].type = Type.BUFF;
                    break;
                case 5:
                    result[i].type = Type.POTION;
                    break;
                default:
                    result[i].type = Type.VIDE;
                    break;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Questions>[] loadQuestions() {
        ArrayList<Questions>[] result = new ArrayList[3];
        for (int i = 0; i < result.length; i++) {
            result[i] = new ArrayList<>();
        }
        for (int i = 0; i < attacks.rowCount(); i++) {
            switch (attacks.getCell(i, 0)) {
                case "1":
                    result[0].add(new Questions(1, attacks.getCell(i, 2), attacks.getCell(i, 3).split(" ")));
                    break;
                case "2":
                    result[1].add(new Questions(1, attacks.getCell(i, 2), attacks.getCell(i, 3).split(" ")));
                    break;
                case "3":
                    result[2].add(new Questions(1, attacks.getCell(i, 2), attacks.getCell(i, 3).split(" ")));
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    public static void resetQuestionsAsked() {
        for (int i = 0; i < QUESTIONSLIST.length; i++) {
            QUESTIONSLIST[0].get(i).setAsked(false);
            QUESTIONSLIST[1].get(i).setAsked(false);
            QUESTIONSLIST[2].get(i).setAsked(false);
        }
    }
}
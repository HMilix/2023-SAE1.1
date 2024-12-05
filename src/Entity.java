import java.util.Arrays;
import java.util.Random;

import utils.MyFunction;

public class Entity {
    int niveau;
    int xp;
    int or;
    int pv;
    int pvmax;
    int mana;
    int manamax;
    int attaque;
    int defense;
    int[] objets;
    boolean[] objetsExiste;
    boolean[] objetsEquipe;
    boolean joueur;

    Entity(int xp, int or, int pv, int pvmax, int attaque, int defense) { // Monstre
        this(0, xp, or, pv, pvmax, 0, 0, attaque, defense, null, null, null, false);
    }

    Entity(int niveau, int xp, int or, int pv, int pvmax, int mana, int manamax, int attaque, int defense, int[] objets,
            boolean[] objetsExiste, boolean[] objetsEquipe, boolean joueur) {
        this.niveau = niveau;
        this.xp = xp;
        this.or = or;
        this.pv = pv;
        this.pvmax = pvmax;
        this.mana = mana;
        this.manamax = manamax;
        this.attaque = attaque;
        this.defense = defense;
        this.objets = objets;
        this.objetsExiste = objetsExiste;
        this.objetsEquipe = objetsEquipe;
        this.joueur = joueur;
    }

    private static Random random = new Random();

    @Override
    public String toString() {
        String result = "xp: " + this.xp + "\n" +
                "or: " + this.or + "\n" +
                "pv: " + this.pv + "\n" +
                "pvmax: " + this.pvmax + "\n" +
                "mana: " + this.mana + "\n" +
                "manamax: " + this.manamax + "\n" +
                "attaque: " + this.attaque + "\n" +
                "defense: " + this.defense + "\n";
        return this.joueur ? "Joueur : \n" + result +
                "Objets: " + Arrays.toString(this.objets) + "\n" +
                "ObjetsExiste: " + Arrays.toString(this.objetsExiste) + "\n" +
                "ObjetEquipe: " + Arrays.toString(this.objetsEquipe) + "\n" : "Monstre : \n" + result;
    }

    // Fonctions
    public boolean battleEnded(Entity m) {
        return (this.pv <= 0 || m.pv <= 0) ? true : false;
    }

    public boolean winner() {
        return this.pv > 0 ? true : false;
    }

    public int calcMaxXP() {
        int xpMax;
        double a = 60;
        double b = 0.2;
        xpMax = (int) (a * Math.exp(b * this.niveau));
        return xpMax;
    }

    public boolean giveLv() {
        if (this.xp >= this.calcMaxXP()) {
            this.niveau = this.niveau + 1;
            return true;
        } else {
            return false;
        }
    }

    public int giveGold(Entity e) {
        this.or += e.or;
        return e.or;
    }

    public int giveXP(Entity e) {
        this.xp += e.xp;
        return e.xp;
    }

    public void regen() {
        this.pv = this.pvmax;
        this.mana = this.manamax;
    }

    public void usePotion(Entity h, Item i) {
        boolean found = false;
        int cpt = 0;
        while (cpt < this.objets.length && !found) {
            if (this.objets[cpt] == i.getId()) {
                found = true;
            } else {
                cpt++;
            }
        }
        if(found) {
            if (Arrays.stream(i.getName().split(" ")).anyMatch("pv"::equals)) {
                this.pv += i.value;
                if (this.pv > this.pvmax) {
                    this.pv = this.pvmax;
                }
                removeItem(i);
            } else {
                this.mana += i.value;
                if (this.mana > this.manamax) {
                    this.mana = this.manamax;
                }
                removeItem(i);
            }
            this.objets[cpt] = 0;
            this.objetsExiste[cpt] = false;
        }
    }

    public void monsterDifficulty(int multiplier) {
        this.niveau = multiplier;
        this.xp = (int) (20 + random.nextInt(31) * multiplier);
        this.or = (int) (100 + random.nextInt(301) * multiplier);
        this.pvmax = (int) (20 + (random.nextInt(20)) * (multiplier));
        this.pv = this.pvmax;
        this.attaque = (int) (5 + ((int) (random.nextInt(10))) * (multiplier * 1.2));
        this.defense = (int) (1 + ((int) (random.nextInt(6))) * (multiplier * 1.2));
    }

    public int attack(Entity e) {
        int degats = random.nextInt(this.attaque - (this.attaque / 2 + 1)) + (this.attaque / 2 + 1) - e.getDefense();
        if (degats > 0) {
            if (e.pv < 0) {
                e.pv = 0;
            }
            return degats;
        } else {
            if (e.pv < 0) {
                e.pv = 0;
            }
            return 1;
        }

    }

    public void printStats(MyFunction printHP, MyFunction printMana) {
        System.out.println("Statistiques " + (this.joueur ? "joueur" : "monstre") + " :");
        System.out.print(this.joueur ? "Niveau      : " + this.niveau + "\n" : "");
        System.out.println("XP          : " + this.xp);
        System.out.println("Or          : " + this.or);
        System.out.print("PV          : ");
        printHP.apply();
        System.out.println();
        if (this.joueur) {
            System.out.print("Mana        : ");
            printMana.apply();
            System.out.println();
        }
        System.out.println("Attaque     : " + this.attaque + "       (" + (this.attaque / 2 + 1) + " - " + this.attaque + ")");
        System.out.println("Défense     : " + this.defense);
    }

    public void toggleItem(Item i, Item[] itemList) {
        boolean found = false;
        int idx = 0;
        while (idx < this.objets.length && !found) {
            if (this.objets[idx] == i.getId()) {
                found = true;
            }
            idx++;
        }
        idx--;
        if (found) {
            if (this.objetsEquipe[idx]) {
                this.objetsEquipe[idx] = false;
            } else {
                this.autoUnequip(i, itemList);
                this.objetsEquipe[idx] = true;
                
            }
        } else {
            System.out.println("Objet non trouvé.");
        }
    }

    public void refreshStats(Item[] items) {
        Entity base = new Entity(1, 0, 200, 30, 30, 20, 20, 10, 4,
                new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                new boolean[] { false, false, false, false, false, false, false, false, false },
                new boolean[] { false, false, false, false, false, false, false, false, false },
                true);
        this.attaque = base.getAttaque();
        this.defense = base.getDefense();

        this.pvmax = base.getPvmax();
        if (this.pv >= this.pvmax) {
            this.pv = this.pvmax;
        }
        this.manamax = base.getManamax();
        if (this.mana >= base.getManamax()) {
            this.mana = this.manamax;
        }
        for (int idx = 0; idx < 9; idx++) {
            if (this.objetsEquipe[idx]) {
                switch (items[this.objets[idx]].getType()) {
                    case ARME:
                        this.attaque = base.getAttaque() + items[this.objets[idx]].getValue();
                        break;
                    case ARMURE:
                        this.defense = base.getDefense() + items[this.objets[idx]].getValue();
                        break;
                    case ACCESSOIRE:
                        if (this.pv == this.pvmax) {
                            this.pvmax = base.getPvmax() + items[this.objets[idx]].getValue();
                            this.pv = this.pvmax;
                        } else {
                            this.pvmax = base.getPvmax() + items[this.objets[idx]].getValue();
                        }
                        break;
                    case BUFF:
                        if (this.mana == this.manamax) {
                            this.manamax = base.getManamax() + items[this.objets[idx]].getValue();
                            this.mana = this.manamax;
                        } else {
                            this.manamax = base.getManamax() + items[this.objets[idx]].getValue();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public boolean addItem(int id) {
        int cpt = 0;
        while (cpt < this.objets.length) {
            if (!this.objetsExiste[cpt]) {
                this.objets[cpt] = id;
                this.objetsExiste[cpt] = true;
                return true;
            } else {
                cpt++;
            }
        }
        return false;
    }

    public boolean inventoryFull() {
        int cpt = 0;
        while (cpt < this.objets.length) {
            if (!this.objetsExiste[cpt]) {
                return false;
            } else {
                cpt++;
            }
        }
        return true;
    }

    public void autoUnequip(Item i, Item[] items) {
        for (int j = 0; j < this.objets.length; j++) {
            if (items[this.objets[j]].getType() == i.getType()) {
                if (this.objetsEquipe[j]) {
                    this.objetsEquipe[j] = false;
                }
            }
        }
    }

    public void removeGold(Item i) {
        this.or -= i.getPrice();
    }

    public boolean removeItem(Item i) {
        boolean found = false;
        int cpt = 0;
        while (cpt < this.objets.length && !found) {
            if (i.getId() == this.objets[cpt]) {
                this.objets[cpt] = 0;
                this.objetsExiste[cpt] = false;
                this.objetsEquipe[cpt] = false;
                return true;
            } else {
                cpt++;
            }
        }
        return false;
    }

    public boolean enoughGold(Item i) {
        if (this.or < i.getPrice()) {
            return false;
        } else {
            return true;
        }
    }

    // Getters
    public int getNiveau() {
        return this.niveau;
    }

    public int getXp() {
        return this.xp;
    }

    public int getOr() {
        return this.or;
    }

    public int getPv() {
        return this.pv;
    }

    public int getPvmax() {
        return this.pvmax;
    }

    public int getMana() {
        return this.mana;
    }

    public int getManamax() {
        return manamax;
    }

    public int getAttaque() {
        return this.attaque;
    }

    public int getDefense() {
        return this.defense;
    }

    public int[] getObjets() {
        return this.objets;
    }

    public boolean[] getObjetsExiste() {
        return this.objetsExiste;
    }

    public boolean[] getObjetsEquipe() {
        return this.objetsEquipe;
    }

    public boolean getJoueur() {
        return this.joueur;
    }

    // Setters
    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setOr(int or) {
        this.or = or;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public void setPvmax(int pvmax) {
        this.pvmax = pvmax;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setManamax(int manamax) {
        this.manamax = manamax;
    }

    public void setAttaque(int attaque) {
        this.attaque = attaque;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setObjets(int[] objets) {
        this.objets = objets;
    }

    public void setObjetsExiste(boolean[] objetsExiste) {
        this.objetsExiste = objetsExiste;
    }

    public void setObjetsEquipe(boolean[] objetsEquipe) {
        this.objetsEquipe = objetsEquipe;
    }
}

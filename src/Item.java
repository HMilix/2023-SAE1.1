class Item {
    int id;      //L'id de l'objet
    Type type;   // Type de l'objet
    String name; // Nom de l'objet
    int price;   // Le prix de l'objet
    int value;   // value représente la valeur de dégâts/défense/soin/etc...

    Item() {
        this(0, Type.VIDE, "", 0, 0); //Empty
    }

    Item(int id, Type type, String name, int price, int value) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.price = price;
        this.value = value;
    }


    @Override
    public String toString() {
        return "ID: " + this.id + "    Type: " + this.type + "    Name: " + this.name + "    price: " + this.price + "    value: " + this.value;
    }

    public boolean isOwned(Entity h) {
        int cpt = 0;
        while (cpt < h.getObjets().length) {
            if (h.getObjetsExiste()[cpt]) {
                if (h.getObjets()[cpt] == this.id) {
                    return true;
                }
            }
            cpt++;
        }
        return false;
    }

    public void printStats() {
        switch (this.type) {
            case ARME:
                System.out.print(" L'objet est une arme.");
                break;
            case ARMURE:
                System.out.print(" L'objet est une armure.");
                break;
            case ACCESSOIRE:
                System.out.print(" L'objet est un accessoire.");
                break;
            case BUFF:
                System.out.print(" L'objet est un boost.");
                break;
            case POTION:
                System.out.print(" L'objet est une potion.");
                break;
            default:
                System.out.print(" Il n'y a pas d'objets sur cette case d'inventaire.");
                break;
        }
        System.out.println();
        if (this.type != Type.VIDE) {
            System.out.println(" Il s'appelle : " + this.name + ".");
            System.out.println(" Son prix est de : " + this.price + " gold.");
            System.out.println(" Sa valeur est de : " + this.value + ".");
        } else {
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    //Getters
    public int getId() {
        return id;
    }
    public Type getType() {
        return type;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public int getValue() {
        return value;
    }
}

import java.util.ArrayList;

class Tests extends Program{
    Entity h = new Entity(1, 0, 200, 30, 30, 20, 20, 10, 4,
                          new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                          new boolean[] { false, false, false, false, false, false, false, false, false },
                          new boolean[] { false, false, false, false, false, false, false, false, false },
                          true);                   
    final static Item[] ITEMSLIST = QuestAttack.loadItems();
    final static ArrayList<Questions>[] QUESTIONSLIST = QuestAttack.loadQuestions();

// ──────────────────────────────────────────── { ENTITY } ────────────────────────────────────────────\\

    void testEnoughGold() {
        assertTrue(h.enoughGold(ITEMSLIST[1]));
        assertFalse(h.enoughGold(ITEMSLIST[2]));
        assertTrue(h.enoughGold(ITEMSLIST[21]));
        assertFalse(h.enoughGold(ITEMSLIST[24]));
        resetEntity();
    }

    void testInventoryFull() {
        assertFalse(h.inventoryFull());
        h.setObjets(new int[]{ 1, 1, 1, 1, 1, 1, 1, 1, 0 });
        h.setObjetsExiste(new boolean[] { true, true, true, true, true, true, true, true, false });
        assertFalse(h.inventoryFull());
        h.setObjets(new int[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1 });
        h.setObjetsExiste(new boolean[] { true, true, true, true, true, true, true, true, true });
        assertTrue(h.inventoryFull());
        resetEntity();
    }

    void testToggleItem() {
        h.setObjets(new int[]{ 1, 2, 0, 7, 8, 0, 14, 15, 0 });
        h.setObjetsExiste(new boolean[] { true, true, false, true, true, false, true, true, false });
        h.setObjetsEquipe(new boolean[] { false, true, false, true, false, false, true, false, false });
 
        h.toggleItem(ITEMSLIST[1], ITEMSLIST);
        assertArrayEquals(new boolean[] { true, false, false, true, false, false, true, false, false }, h.getObjetsEquipe());

        h.toggleItem(ITEMSLIST[8], ITEMSLIST);
        assertArrayEquals(new boolean[] { true, false, false, false, true, false, true, false, false }, h.getObjetsEquipe());

        h.toggleItem(ITEMSLIST[12], ITEMSLIST);
        assertArrayEquals(new boolean[] { true, false, false, false, true, false, true, false, false }, h.getObjetsEquipe());
        resetEntity();
    }

    void testAddItem() {
        assertTrue(h.addItem(1));
        assertArrayEquals(new int[]{ 1, 0, 0, 0, 0, 0, 0, 0, 0 }, h.getObjets());
        assertArrayEquals(new boolean[] { true, false, false, false, false, false, false, false, false }, h.getObjetsExiste());
        h.setObjets(new int[]{ 1, 1, 1, 1, 1, 1, 1, 1, 0 });
        h.setObjetsExiste(new boolean[] { true, true, true, true, true, true, true, true, false });
        assertTrue(h.addItem(5));
        assertArrayEquals(new int[]{ 1, 1, 1, 1, 1, 1, 1, 1, 5 }, h.getObjets());
        assertArrayEquals(new boolean[] { true, true, true, true, true, true, true, true, true }, h.getObjetsExiste());
        assertFalse(h.addItem(15));
        resetEntity();
    }

// ──────────────────────────────────────────── { QUESTIONS } ────────────────────────────────────────────\\

    void testAsk() {
        assertTrue(QUESTIONSLIST[0].get(0).ask());
        assertFalse(QUESTIONSLIST[0].get(0).ask());
        assertTrue(QUESTIONSLIST[1].get(0).ask());
        assertTrue(QUESTIONSLIST[2].get(2).ask());
        assertFalse(QUESTIONSLIST[2].get(2).ask());
    }

    void testValid() {
        assertTrue(QUESTIONSLIST[0].get(0).valid("for"));
        assertFalse(QUESTIONSLIST[0].get(0).valid("while"));
        assertTrue(QUESTIONSLIST[1].get(1).valid("v"));
        assertTrue(QUESTIONSLIST[1].get(1).valid("vrai"));
        assertFalse(QUESTIONSLIST[1].get(1).valid("faux"));
        assertTrue(QUESTIONSLIST[2].get(2).valid("switch"));
        assertTrue(QUESTIONSLIST[2].get(2).valid("label"));
        assertFalse(QUESTIONSLIST[2].get(2).valid("jambon"));
        assertFalse(QUESTIONSLIST[2].get(2).valid(""));
        assertFalse(QUESTIONSLIST[2].get(2).valid(" "));
    }

// ──────────────────────────────────────────── { ITEM } ────────────────────────────────────────────\\

    void testIsOwned() {
        h.setObjets(new int[]{ 1, 2, 0, 7, 8, 0, 14, 15, 0 });
        h.setObjetsExiste(new boolean[] { true, true, false, true, true, false, true, true, false });
        assertTrue(ITEMSLIST[2].isOwned(h));
        assertTrue(ITEMSLIST[15].isOwned(h));
        assertFalse(ITEMSLIST[5].isOwned(h));
        assertFalse(ITEMSLIST[22].isOwned(h));
        resetEntity();
    }




    void resetEntity() {
        h = new Entity(1, 0, 200, 30, 30, 20, 20, 10, 4,
                       new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                       new boolean[] { false, false, false, false, false, false, false, false, false },
                       new boolean[] { false, false, false, false, false, false, false, false, false },
                       true);
    }    
}

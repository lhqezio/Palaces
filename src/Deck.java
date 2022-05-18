import java.util.concurrent.ThreadLocalRandom;

//Deck class that contains all the necessary methods for the game.
//Length and index numbering is similar to the one of a usual Array
public class Deck {
    final private Card[] deckCard;
    private int length;

    public Deck() {
        length = 0;
        //Every Deck is 0 at initialize
        //Growing,Desizing,Regrowing accordingly to the process of removing and adding card.
        deckCard = new Card[104];
    }

    public int length() {
        return this.length;
    }

    public Card getCard(int index) {
        if (index < 0 || index >= length) {
            throw new ArrayIndexOutOfBoundsException("Index must be between 0 and " + (length - 1));
        }
        return deckCard[index];
    }

    public void put(Card card) {
        this.length++;
        this.deckCard[length - 1] = card;
    }

    public void pull(int index) {
        if (index < 0 || index >= length) {
            throw new ArrayIndexOutOfBoundsException("Index must be between 0 and " + (length - 1));
        }
        this.length--;
        //"push" the cards down to fill space
        while (index < length) {
            deckCard[index] = deckCard[index + 1];
            index++;
        }
        //Set null to make the position actually empty. Optional
        deckCard[length] = null;
    }

    private void swap(int index1, int index2) {
        Card temp = deckCard[index1];
        deckCard[index1] = deckCard[index2];
        deckCard[index2] = temp;
    }

    public void sort() {
        for (int i = 0; i < this.length - 1; i++) {
            if (deckCard[i].getValue() > deckCard[i + 1].getValue()) {
                swap(i, i + 1);
                for (int z = i; z != 0; z--) {
                    if (deckCard[z].getValue() < deckCard[z - 1].getValue()) {
                        swap(z, z - 1);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public void shuffle() {
        Deck original = new Deck();
        for (int i = 0; i < length; i++) {
            original.put(deckCard[i]);
        }
        for (int i = 0; i < this.length(); i++) {
            int r = ThreadLocalRandom.current().nextInt(0, original.length());
            deckCard[i] = original.getCard(r);
            original.pull(r);
        }
    }

    public void purge() {
        this.length = 0;
    }
}

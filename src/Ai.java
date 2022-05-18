public class Ai {
    public static Deck think(Card card, Deck deck) {
        Deck nonWild = new Deck();
        Deck wild = new Deck();
        //Separate between Wild Cards and Non-Wild to two different decks.
        for (int i = 0; i < deck.length(); i++) {
            if (deck.getCard(i).getValue() != 2 && deck.getCard(i).getValue() != 10) {
                nonWild.put(deck.getCard(i));
            } else {
                wild.put(deck.getCard(i));
            }
        }
        // Sort the two so minimum would be the first to appear
        nonWild.sort();
        wild.sort();
        //As suit doesn't matter in this game, report the first playable card in non-wild pile(Since it's sorted it's now minimum)
        //and then look for cards with the same value and add those to the playable deck
        boolean noCard = true;
        Deck cardsToPlay = new Deck();
        if (nonWild.length() != 0) {
            for (int i = 0; i < nonWild.length() && noCard; i++) {
                if (nonWild.getCard(i).getValue() >= card.getValue()) {
                    cardsToPlay.put(nonWild.getCard(i));
                    noCard = false;
                    i++;
                    while (i < nonWild.length()) {
                        if (nonWild.getCard(i).getValue() == cardsToPlay.getCard(0).getValue()) {
                            cardsToPlay.put(nonWild.getCard(i));
                            i++;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        //if there's wild card and no playable non-wild return the first wild card (it would be min since deck is sorted). Don't check for other reoccurences
        if (wild.length() != 0 && noCard) {
            cardsToPlay.put(wild.getCard(0));
        }
        //if playable is empty program will instruct the AI/or recommend player to pick the deck up
        return cardsToPlay;
    }

    public static Deck think(Deck deck) {
        Deck cardsToPlay = new Deck();
        Deck nonWild = new Deck();
        Deck wild = new Deck();
        //Separate between Wild Cards and Non-Wild to two different decks.
        for (int i = 0; i < deck.length(); i++) {
            if (deck.getCard(i).getValue() != 2 && deck.getCard(i).getValue() != 10) {
                nonWild.put(deck.getCard(i));
            } else {
                wild.put(deck.getCard(i));
            }
        }
        // Sort the two so minimum would be the first to appear
        nonWild.sort();
        wild.sort();
        if (nonWild.length() != 0) {
            cardsToPlay.put(nonWild.getCard(0));
            for (int i = 1; i < nonWild.length(); i++) {
                if (nonWild.getCard(i).getValue() == cardsToPlay.getCard(0).getValue()) {
                    cardsToPlay.put(nonWild.getCard(i));
                } else {
                    break;
                }
            }
        } else if (wild.length() != 0) {
            cardsToPlay.put(wild.getCard(0));
        }
        return cardsToPlay;
    }

    //Using the think method to get cards then give the best advice in the form of string.
    public static String recommend(Card card, Deck deck) {
        StringBuilder recmd = new StringBuilder();
        Deck toPlay = think(card, deck);
        if (toPlay.length() == 0) {
            recmd.append("Pick the pile of shame");
        } else {
            recmd.append("Play: ");
            for (int i = 0; i < toPlay.length(); i++) {
                recmd.append(toPlay.getCard(i).toString()).append(" ");
            }
        }
        return recmd.toString();
    }

    public static String recommend(Deck deck) {
        StringBuilder recmd = new StringBuilder();
        Deck toPlay = think(deck);
        if (toPlay.length() == 0) {
            recmd.append("Pick the pile of shame");
        } else {
            recmd.append("Play: ");
            for (int i = 0; i < toPlay.length(); i++) {
                recmd.append(toPlay.getCard(i).toString()).append(" ");
            }
        }
        return recmd.toString();
    }

}

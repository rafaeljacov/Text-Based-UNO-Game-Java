import java.util.Stack;

public class PlayMethods {
    static void cardUnplayable() {
        System.out.println("Unable to play card chosen: Card mismatch from discard pile");
    }

    static void dealCards(Player currentPlayer) {
        // Deal 7 cards to each player
        System.out.println("Dealing 7 cards to each player...");
        while (currentPlayer.hand.size() < 7) {
            Card dealtCard = Play.deck.pop();
            currentPlayer.hand.push(dealtCard);
            currentPlayer = currentPlayer.next;
        }
    }

    static void displayHand(Player player) {
        System.out.printf("%s's Hand:\n", player.name);
        int i = 0;
        for (Card card: player.hand) {
            if (player instanceof User) {
                System.out.printf("\t[%d] ", i+1);
                Card.printCard(card);
            } else {
                System.out.print("\t");
                Card.printCard(card);
            }
            i++;
        }
        System.out.println();
    }

    static void drawCards(int quantity, Player player) {
        for (int i = 0; i < quantity; i++) {

            // Reshuffle if Draw Pile is empty.
            if (Play.deck.isEmpty()) {
                System.out.println("""
                        Draw Pile is currently empty.
                        Transferring cards from Discard Pile except top card to Draw Pile and reshuffling...
                        """);

                System.out.println("\nDone.");

                Card topDiscardPile = Play.discardPile.pop();
                Play.deck.addAll(Play.discardPile);
                Play.discardPile.removeAllElements();

                Deck.shuffle(Play.deck);
                Play.discardPile.push(topDiscardPile);
            }

            // Draw Card.
            Card drawnCard = Play.deck.pop();
            player.hand.push(drawnCard);
        }
    }

    static void startDiscardPile(Stack<Card> drawPile, Stack<Card> discardPile) {
        // Keep drawing to discard pile if the drawn card is not a NumberCard.
        while(true) {
            Card topCard = drawPile.pop();
            discardPile.push(topCard);
            if (topCard instanceof NumberCard) break;
        }
    }

    static int[] scanPlayableCards(Player player, Card topDiscardPile, String colorToPlay) {
        // Array of playable cards:
        // First index indicates count of playable cards except wild draw 4 cards,
        // Second index indicates wild draw 4 cards.
        int[] playable = {0, 0};

        // Check if each card in hand is playable.
        for (Card card: player.hand) {
            // Check for Wild Draw 4.
            if(card instanceof WildCard && ((WildCard) card).type.equals("Wild Draw 4")) playable[1]++;
            // Check for others
            else if (card.isPlayable(topDiscardPile, colorToPlay)) playable[0]++;
        }
        return playable;
    }

    static void printDeckStats() {
        System.out.println("*************************************************************");
        System.out.printf("\tDraw Pile card count: %d, Discard Pile card count: %d\n",
                Play.deck.size(), Play.discardPile.size());
        System.out.print("\t\t\tTop Card of Discard Pile: ");
        Card.printCard(Play.discardPile.peek());
        System.out.printf("\t\t\t\t\tColor to play: %s\n", Play.getColorToPlay());
        System.out.println("*************************************************************");
    }

    static void checkWinner(Player player) {
        if (player.hand.isEmpty()) {
            Play.setWinner(player);
        }
    }
}

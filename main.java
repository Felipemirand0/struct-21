import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

class Card {
    public String naipe;
    public String name;
    public int value;

    public Card(String naipe, String name, int value) {
        this.name = name;
        this.naipe = naipe;
        this.value = value;
    }
}

class Pack {
    private List<Card> cards = new ArrayList<Card>();

    public Pack(int level) {
        Integer[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        String[] letters = { "J", "Q", "K" };
        String[] naipes = { "Clubs", "Diamonds", "Hearts", "Spades" };

        level = (level > 7 ? 7 : (level < 1 ? 1 : level));

        // level - pack quant
        for (int k = 0; k < level; k++) {
            // create packs
            for (int j = 0; j < naipes.length; j++) {
                for (int i = 0; i < letters.length; i++) {
                    Card newCard = new Card(naipes[j], letters[i].toString(), 10);
                    cards.add(newCard);
                }
                for (int i = 0; i < numbers.length; i++) {
                    Card newCard = new Card(naipes[j], numbers[i].toString(), numbers[i]);
                    cards.add(newCard);
                }
            }
        }
    }

    // methods
    public void shuffle() {
        Collections.shuffle(this.cards);
        // System.out.println("\n\n-------------------------- Shuffled pack --------------------------\n");
        // this.showCards();
    }

    public Card getCard() {
        Card lastCard = this.cards.get(this.cards.size() - 1);
        this.cards.remove(this.cards.size() - 1);

        System.out.println(
                String.format("Card -> [%s] of [%s] - value [%d]", lastCard.name, lastCard.naipe, lastCard.value));
        return lastCard;
    }

    public void showCards() {
        for (int i = 0; i < this.cards.size(); i++) {
            Card atualCard = this.cards.get(i);
            System.out.println(
                    String.format("[%s] of [%s] - value [%d]", atualCard.name, atualCard.naipe, atualCard.value));
        }
    }
}

class Player {
    private String _name;
    private int _victories;
    private List<Card> _cards = new ArrayList<Card>();

    // sets
    public Player(String name) {
        this._name = name;
    }

    public void addCard(Card card) {
        this._cards.add(card);
    }

    public void addVictory() {
        this._victories += 1;
    }

    public void clear() {
        this._cards.clear();
    }

    // gets
    public String getName() {
        return this._name.toUpperCase();
    }

    public List<Card> getCards() {
        return this._cards;
    }

    public int getVictories() {
        return this._victories;
    }

    public int points() {
        int points = 0;

        for (int i = 0; i < this._cards.size(); i++) {
            points += this._cards.get(i).value;
        }

        return points;
    }
}

class Game21 {
    public int verifyWinner(List<Card> cards) {
        int points = 0;
        boolean containsAs = false;
        
        for (int i = 0; i < cards.size(); i++ ) {
            Card card = cards.get(i);
            
            if (card.name == "1" || card.name.equals("1")) {
                containsAs = true;
            }
            points += card.value;
        }

        if(containsAs) {
            points += (points + 10 < 21 ? 0 : 10);  
        }

        return points;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Add players
        List<Player> players = new ArrayList<Player>();

        for (int i = 0; i < 2; i++) {
            Player player = new Player(i == 0 ? "Player" : "Computer");
            players.add(player);
        }


        // Init
        boolean continueGame = true;

        while (continueGame) {
            System.out.print("\n\n-------------------------- Twenty one (pilots?) --------------------------");

            System.out.println("\n\nWhat level of difficulty is desired? (1-7)");
            System.out.print("Answer: ");
            int level = input.nextInt();

            Pack pack = new Pack(level);

            // Show pack
            // pack.showCards();

            // Shuffle pack 
            pack.shuffle();

            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                player.clear();
                // Get two cards
                System.out.println(String.format("\n\n--- [%s] cards: ", player.getName()));
                player.addCard(pack.getCard());
                player.addCard(pack.getCard());

                boolean newCard = true;

                // Get new cards
                while (newCard) {
                    System.out.println("\n\nWant more one card? ('y' for yes or any other letter for not)");
                    System.out.print("Answer: ");
                    String answer = input.next();

                    if (answer.toLowerCase().equals("y")) {
                        System.out.print("\n"); // isso é para ficar menos feio
                        player.addCard(pack.getCard());

                        System.out.println(String.format("\nAtual points: [%d]\n", player.points()));
                        
                        if (player.points() > 21) {
                            System.out.println(String.format("\n\n----[%s] LOST ----\n\n", player.getName()));
                            newCard = false;
                        }
                    } else {
                        System.out.println(String.format("\n[%s] points: [%d]\n\n", player.getName(), player.points()));
                        newCard = false;
                    }
                }
            }

            if (players.get(0).points() > 21) {
                players.get(1).addVictory();
            } else if (players.get(1).points() > 21) {
                players.get(0).addVictory();
            } else {
                Game21 game = new Game21();
                int winner = game.verifyWinner(players.get(0).getCards()) > game.verifyWinner(players.get(1).getCards()) ? 0 : 1;
                
                System.out.println(String.format("\n\n\n ------ [%s] WINNER - [%d] POINTS ----- nn\n\n\n", players.get(winner).getName(), players.get(winner).points()));

                players.get(winner).addVictory();
            }
            
            System.out.println("Number of victories first player; " +  players.get(0).getVictories());
            System.out.println("Number of victories second player; " +  players.get(1).getVictories());
            
            System.out.println("\n\nDo you want to play again? ('y' for yes or any other letter for not)");
            System.out.print("Answer: ");
            String continueBool = input.next();

            continueGame = (continueBool.toLowerCase().equals("y"));
        }
    }
}
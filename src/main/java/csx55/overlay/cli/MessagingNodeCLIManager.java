package csx55.overlay.cli;

import csx55.overlay.node.MessagingNode;

import java.util.Scanner;

public class MessagingNodeCLIManager implements Runnable, CLIManager {

    private MessagingNode node;

    public MessagingNodeCLIManager(MessagingNode node) {
        this.node = node;
    }

    public void run() {
        String input = "";
        Scanner sc = new Scanner(System.in);
        while (!(input = sc.nextLine()).equals("quit")) {
            processInput(input);
        }
        sc.close();
        System.exit(0);
    }

    public void processInput(String input) {
        String[] parsedInput = input.split(" ");
        String command = parsedInput[0];
        switch(command) {
            case ("exit-overlay"):
                this.node.deregisterSelf();
                break;
            case ("list-partners"):
                this.node.listPartners();
                break;
            case ("print-shortest-path"):
                this.node.printPaths();
                break;
            case ("poke"):
                try {
                    String partner = parsedInput[1];
                    this.node.pokePartner(partner);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("ERROR you must provide a message (String) argument. " + e);
                }
                break;
            default:
                System.out.println("Invalid CLI Input: " + input);
        }
    }

}

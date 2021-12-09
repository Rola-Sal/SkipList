package HW2;

import java.io.*;
import java.util.*;


class Node {
    public Node prev;
    public Node next;
    public Node up;
    public Node down;

    public int key;

    public Node(int key) {
        this.key = key;
        this.prev = null;
        this.next = null;
        this.up = null;
        this.down = null;
    }
}

public class SkipList {
    private Node head;
    private Node tail;

    private final int negTnfinity = Integer.MIN_VALUE;
    private final int posTnfinity = Integer.MAX_VALUE;

    private int heightOfSkipList;



    public SkipList() {
        head = new Node(negTnfinity);
        tail = new Node(posTnfinity);

        head.next = tail;
        tail.prev = head;

        heightOfSkipList = 1; //first we use one level


    }

    public boolean getCoinFlip() {
        return new Random().nextBoolean();
    }

    public Node search(int key) {
        Node node = head;

        while (node.down != null) { //when node.down = null so it is the bottom linked list L0
            node = node.down;
            while (node.next.key <= key) {
                node = node.next;
            }
        }
        return node;

    }

    public void skipInsert(int key) {
        Node newNode = new Node(key);
        Node foundNode = search(key);

        if (foundNode.key == key) {
            return;
        }

        int currentLevel = 0;

        insertNode(foundNode, newNode);
        currentLevel++;

        while (getCoinFlip()) {
            if (currentLevel >= heightOfSkipList) {
                addNewLevel();
            }
            //go prev until find a node with up
            while (foundNode.up == null)
                foundNode = foundNode.prev;
            //go up
            foundNode = foundNode.up;

            Node copyFromNewNode = new Node(key);
            insertNode(foundNode, copyFromNewNode);
            copyFromNewNode.down = newNode;
            newNode.up = copyFromNewNode;

            newNode = copyFromNewNode;
            currentLevel++;

        }
    }

    public void insertNode(Node prevNode, Node newNode) {
        if (prevNode == null) {
            return;
        }
        newNode.prev = prevNode;
        newNode.next = prevNode.next;
        prevNode.next.prev = newNode;
        prevNode.next = newNode;

    }

    public void addNewLevel() {
        Node newHead = new Node(negTnfinity);
        Node newTail = new Node(posTnfinity);

        newHead.next = newTail;
        newHead.down = this.head;
        newTail.prev = newHead;
        newTail.down = this.tail;

        this.head.up = newHead;
        this.tail.up = newTail;

        this.head = newHead;
        this.tail = newTail;

        this.heightOfSkipList++;
    }

    public void delete(int key) {
        Node foundNode = search(key);
        if (foundNode == null || foundNode.key != key) {

            return;
        }

        while (foundNode != null) {
            foundNode.prev.next = foundNode.next;
            foundNode.next.prev = foundNode.prev;
            foundNode.down = null;
            foundNode = foundNode.up;
        }

    }

    public int getMax() {
        Node posInfinityNode = this.tail;
        while (posInfinityNode.down != null) {
            posInfinityNode = posInfinityNode.down;
        }
        if (posInfinityNode.prev.key != this.negTnfinity) {
            Node maxNode = posInfinityNode.prev;
            return maxNode.key;
        } else return this.posTnfinity;
    }

    public int getMin() {
        Node negInfinityNode = this.head;
        while (negInfinityNode.down != null) {
            negInfinityNode = negInfinityNode.down;
        }
        if (negInfinityNode.next.key != this.posTnfinity) {
            Node minNode = negInfinityNode.next;
            return minNode.key;
        } else
            return this.negTnfinity;
    }

    public void printSkipList() {
        String skipList = "[";

        Node currentHead = this.head;
        while (currentHead.down != null) {
            currentHead = currentHead.down;
        }
       while(currentHead.next.key != this.posTnfinity) {
           currentHead = currentHead.next;
            skipList += currentHead.key;
            if (currentHead.next.key != this.posTnfinity) skipList += ",";
            else skipList += "]";
        }

        System.out.println(skipList);
    }

    public static void main(String[] args) {
        SkipList skipList = new SkipList();

        Scanner scanner;
        scanner = new Scanner(System.in);
        int numOfNodes = scanner.nextInt();
        int numOfOperations = scanner.nextInt();
        String [] commands = new String[numOfOperations];
        int [] keys = new int[numOfOperations];

        ArrayList<String> searchResult = new ArrayList<>();

        String line;
        line = scanner.nextLine();
        for (int i = 0; i < numOfOperations; i++) {
            line = scanner.nextLine();
            String[] parts = line.split(" ");
            String command = parts[1];
            int key = Integer.parseInt(parts[3]);
            commands[i] = command;
            keys[i] = key;
        }

        for (int i=0 ; i < numOfOperations ; i++){
            switch (commands[i]) {
                case "i":
                    skipList.skipInsert(keys[i]);
                    break;
                case "d":
                    skipList.delete(keys[i]);
                    break;
                case "s":
                    Node foundNode = skipList.search(keys[i]);
                    if (foundNode.key == keys[i]) //it will return the key node or the largest node smaller than the key
                        searchResult.add("[Yes]");
                    else
                        searchResult.add("[No]");

            }
        }
        searchResult.forEach(result -> System.out.println(result));
        int max = skipList.getMax();
        System.out.println("[" + max + "]");

        int min = skipList.getMin();
        System.out.println("[" + min + "]");

        skipList.printSkipList();
        System.gc();
        System.exit(0);


    }


}


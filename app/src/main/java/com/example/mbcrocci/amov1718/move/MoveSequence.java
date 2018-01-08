package com.example.mbcrocci.amov1718.move;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class MoveSequence implements Serializable {
    // linked list
    private static class Node<T> {
        T data;
        Node<T> next, prev;

        public Node(T data) {
            super();
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private static final long serialVersionUID = 1L;

    private Node<Move> head, last, current;

    public MoveSequence() {
        super();
        this.head = null;
        this.current = null;
        this.last = null;
    }

    public void append(Move m) {
        if(head == null) {
            head = new Node<Move>(m);
            head.prev = null;
            last = head;
            current = head;
        }
        else {
            last.next = new Node<Move>(m);
            Node<Move> prev = last;
            last = last.next;
            last.prev = prev;
        }
    }
    public void delete() {
        if(isEmpty())
            throw new NoSuchElementException("Sequence is empty.");

        if(last == head) {
            head = null;
            last = null;
            current = null;
        }
        else {
            if(current == last)
                current = last.prev;
            last = last.prev;
            last.next = null;
        }
    }

    public Move currentMove() {
        return current == null ? null : current.data;
    }

    public boolean hasNext() {
        return current != null && current.next != null;
    }

    public boolean hasPrevious() {
        return current != null && current.prev != null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void lastMove() {
        if(current == null)
            current = last;
        else
            current = current.prev;
    }

    public void nextMove() {
        if(current == null)
            return;

        current = current.next;
    }

    public void reset() {
        current = head;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<Move> node = head;
        while(node != null) {
            sb.append(node.data.toString());
            sb.append('|');
            node = node.next;
        }
        return sb.toString();
    }

    public static MoveSequence fromString(String str) {
        StringTokenizer st = new StringTokenizer(str, "|");
        MoveSequence ms = new MoveSequence();

        while(st.hasMoreTokens()) {
            String tok = st.nextToken();
            int pos = tok.indexOf('-');
            Move m = new Move(tok.substring(0, pos), tok.substring(pos + 1));
            pos = tok.indexOf('=');
            if(pos != -1)
                m.setSpecial(Move.Special.parseChar(tok.charAt(pos + 1)));
            ms.append(m);
        }
        return ms;
    }
}

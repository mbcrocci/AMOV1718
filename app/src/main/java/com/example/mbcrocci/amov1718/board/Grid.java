package com.example.mbcrocci.amov1718.board;

public interface Grid<E> {

    // Devolve quem ocupa loc
    public E get(Location loc);


    // inverte a grid para ser mostradra ao contrario
    public void invert();

    public boolean isInverted();

    // numero de colunas
    public int getCols();

    // numero de linhas
    public int getRows();

    public boolean isOccupied(Location loc);

    public boolean isValid(Location loc);

    // posiciona um objecto numa localizacao
    // devolve o antigo ocupante ou null se estivesse vazia
    public E put(Location loc, E obj);

    public void remove(Location loc);

    public String draw();
}

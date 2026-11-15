package model;

// everything we save to a file can give one text line
public interface Storable {

    String toLine();
}
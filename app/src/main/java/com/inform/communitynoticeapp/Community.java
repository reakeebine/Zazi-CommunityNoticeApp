package com.inform.communitynoticeapp;

/**
 * @author Lehasa Seoe (SXXLEH001) Rea Keebine (KBNREA001) Dineo Magakwe (MGKDIN001)
 * 06 October 2021
 * Model class for the communities
 */
@SuppressWarnings("JavaDoc")
public class Community {
    private String name;

    /**
     * Empty Constructor
     */
    public Community() {}

    /**
     * One argument constructor
     * @param name
     */
    public Community(String name) {
        this.name = name;
    }

    /**
     * getter method for community
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for community
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}

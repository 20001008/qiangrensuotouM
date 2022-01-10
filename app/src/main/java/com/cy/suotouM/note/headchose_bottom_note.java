package com.cy.suotouM.note;

public class headchose_bottom_note {
    private String Name;
    private boolean isselect;
    public headchose_bottom_note(String Name,boolean isselect)
    {
        this.Name=Name;
        this.isselect=isselect;
    }
    public boolean getisselect()
    {
        return this.isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }

    public String getName() {
        return Name;
    }

}

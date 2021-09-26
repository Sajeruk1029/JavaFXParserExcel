package com.example.parserexcelbellsoftmavenfx.Models;

public class TemplateThree extends TemplateTwo{

    protected boolean Availability;

    public TemplateThree(){}

    public TemplateThree(int id, String name, String description, long value, boolean availability){
        this.Id = id;
        this.Name = name;
        this.Description = description;
        this.Value = value;
        this.Availability = availability;
    }

    public boolean getAvailability(){ return this.Availability; }

    public void setAvailability(boolean availability){ this.Availability = availability; }
}

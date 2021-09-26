package com.example.parserexcelbellsoftmavenfx.Models;

public class TemplateTwo extends TemplateOne{

    protected Long Value;

    public TemplateTwo(){}

    public TemplateTwo(int id, String name, String description, long value){
        this.Id = id;
        this.Name = name;
        this.Description = description;
        this.Value = value;
    }

    public long getValue(){ return this.Value; }

    public void setValue(long value){ this.Value = value; }
}

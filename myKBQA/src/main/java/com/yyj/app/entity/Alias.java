package com.yyj.app.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;


import java.io.Serializable;



@Data
@NoArgsConstructor
@NodeEntity(label = "Alias")
public class Alias implements Serializable{
    @Id
    @GeneratedValue
    private long id;

    @Property
    private String name;

    @Property
    private String label = "0";

    @Override
    public String toString() {
        return name+"::"+label;
    }
}

package com.yyj.app.dao;

import com.yyj.app.entity.*;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PestRepository extends Neo4jRepository<Pest,Long> {
    //防治方法
    @Query("match (n:Pest {name:$from})-[:hasPrevention]-(m:Prevention) return m order by m")
    List<Prevention> findPestPrevention(@Param("from") String from);
    //症状
    @Query("match (n:Pest {name:$from})-[:hasSymptom]-(m:Symptom) return m order by m")
    List<Symptom> findPestSymptom(@Param("from") String from);
    //危害规律
    @Query("match (n:Pest {name:$from})-[:hasRules]-(m:Rules) return m order by m")
    List<Rules> findPestRules(@Param("from") String from);
    //防治药剂
    @Query("match (n:Pest {name:$from})-[:hasChemicals]-(m:Chemicals) return m order by m")
    List<Chemicals> findPestChemicals(@Param("from") String from);
    //为害部位
    @Query("match (n:Pest {name:$from})-[:damageParts]-(m:Parts) return m order by m")
    List<Parts> findPestParts(@Param("from") String from);
    //为害植物
    @Query("match (n:Pest {name:$from})-[:damagePlants]-(m:Plants) return m order by m")
    List<Plants> findPestPlants(@Param("from") String from);
}

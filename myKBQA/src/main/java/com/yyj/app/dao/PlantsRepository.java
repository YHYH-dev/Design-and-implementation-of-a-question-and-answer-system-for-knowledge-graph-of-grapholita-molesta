package com.yyj.app.dao;

import com.yyj.app.entity.*;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PlantsRepository extends Neo4jRepository<Plants,Long> {

    //别名
    @Query("match (n:Plants {name:$from})-[:hasAlias]-(m:Alias) return m order by m")
    List<Alias> findPlantsAlias(@Param("from") String from);
    //拉丁名
    @Query("match (n:Plants {name:$from})-[:hasLatinName]-(m:LatinName) return m order by m")
    List<LatinName> findPlantsLatinName(@Param("from") String from);
    //简介
    @Query("match (n:Plants {name:$from})-[:hasSummary]-(m:Summary) return m order by m")
    List<Summary> findPlantsSummary(@Param("from") String from);
    //分类学
    @Query("match (n:Plants {name:$from})-[:hasTaxonomy]-(m:Taxonomy) return m order by m")
    List<Taxonomy> findPlantsTaxonomy(@Param("from") String from);
}

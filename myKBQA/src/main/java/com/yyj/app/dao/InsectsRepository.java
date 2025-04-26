package com.yyj.app.dao;

import com.yyj.app.entity.*;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsectsRepository extends Neo4jRepository<Insects,Long> {
    //别名
    @Query("match (n:Insects {name:$from})-[:hasAlias]-(m:Alias) return m order by m")
    List<Alias> findInsectsAlias(@Param("from") String from);
    //拉丁名
    @Query("match (n:Insects {name:$from})-[:hasLatinName]-(m:LatinName) return m order by m")
    List<LatinName> findInsectsLatinName(@Param("from") String from);
    //简介
    @Query("match (n:Insects {name:$from})-[:hasSummary]-(m:Summary) return m order by m")
    List<Summary> findInsectsSummary(@Param("from") String from);
    //分类学
    @Query("match (n:Insects {name:$from})-[:hasTaxonomy]-(m:Taxonomy) return m order by m")
    List<Taxonomy> findInsectsTaxonomy(@Param("from") String from);
    //繁殖方式
    @Query("match (n:Insects {name:$from})-[:hasReproduction]-(m:Reproduction) return m order by m")
    List<Reproduction> findInsectsReproduction(@Param("from") String from);
    //生活习性
    @Query("match (n:Insects {name:$from})-[:hasHabit]-(m:Habit) return m order by m")
    List<Habit> findInsectsHabit(@Param("from") String from);
    //分布区域
    @Query("match (n:Insects {name:$from})-[:distributedIn]-(m:Location) return m order by m")
    List<Location> findInsectsLocation(@Param("from") String from);
    //形态特征
    @Query("match (n:Insects {name:$from})-[:hasFeatures]-(m:Features) return m order by m")
    List<Features> findInsectsFeatures(@Param("from") String from);
    //天敌
    @Query("match (n:Insects {name:$from})-[:hasEnemy]-(m:Insects) return m order by m")
    List<Insects> findInsectsEnemy(@Param("from") String from);

}

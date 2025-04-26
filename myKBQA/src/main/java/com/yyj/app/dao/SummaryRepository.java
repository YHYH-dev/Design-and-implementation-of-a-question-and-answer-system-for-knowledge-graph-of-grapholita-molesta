package com.yyj.app.dao;

import com.yyj.app.entity.Summary;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummaryRepository extends Neo4jRepository<Summary,Long> {


    //    List<Summary> findSummary(@Param("from") String from);

    //    @Query("match (n {name:{from}})-[:hasSummary]-(m:Summary) return m order by m")
//    @Query("MATCH (n)-[:hasSummary]->(m:Summary) WHERE n.name = from RETURN m order by m")
    @Query("match (n {name:$from})-[:hasSummary]-(m:Summary) return m order by m")
    List<Summary> findSummary(@Param("from") String from);

//    @Query("match (n {name:{from}})-[:{relation}}]-(m {name:{to}}) return m order by m")
//    List<Summary> findExistence(@Param("from") String from,@Param("relation") String relation,@Param("to") String to);

    @Query("match (n {name:$from})-[:damagePlants]-(m {name:$to}) return m order by m")
    List<Summary> findExistence(@Param("from") String from,@Param("relation") String relation,@Param("to") String to);

//    Boolean findExistenceResult(@Param("from") String from,@Param("relation") String relation,@Param("to") String to);
}

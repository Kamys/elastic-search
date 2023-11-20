package com.example

import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.data.elasticsearch.core.query.CriteriaQuery
import org.springframework.stereotype.Service

@Service
class ProjectSearchService(
    private val elasticsearchOperations: ElasticsearchOperations
) {

    fun searchProjects(query: String): List<Project> {
        val criteria = Criteria("name").boost(2.0f)
            .matches(query).or("description").matches(query)

        val searchQuery = CriteriaQuery(criteria)

        return elasticsearchOperations.search(searchQuery, Project::class.java)
            .searchHits
            .map { it.content }
    }
}
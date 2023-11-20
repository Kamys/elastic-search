package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val context = runApplication<Application>(*args)
    val repository = context.getBean(ProjectRepository::class.java)
    val searchService = context.getBean(ProjectSearchService::class.java)
    while (true) {
        print("Search text:")
        val search = readln()
        val result = searchService.searchProjects(search)
        println("==Result ${result.size}==")
        result.forEach {
            println("${it.name} - ${it.description}")
        }
        println("=======")
    }
}

fun ProjectRepository.createProjects() {
    this.save(Project(
        id = "1",
        name = "ТехноИмпульс",
        description = "Инновационный проект в области высоких технологий и робототехники"
    ))

    this.save(Project(
        id = "2",
        name = "ЭкоСинтез",
        description = "Экологический проект, направленный на разработку устойчивых энергетических решений"
    ))

    this.save(Project(
        id = "3",
        name = "КиберАвангард",
        description = "Проект в области кибербезопасности и защиты данных"
    ))

    this.save(Project(
        id = "4",
        name = "МедиаСтрим",
        description = "Проект по созданию инновационной платформы для медиа-контента и стриминга"
    ))
}

@Document(indexName = "projects")
class Project(
    @Id
    var id: String? = null,
    val name: String,
    val description: String? = null
)

interface ProjectRepository : ElasticsearchRepository<Project, String> {
    fun findByName(name: String): List<Project>
}

package com.example

import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class ProjectSearchServiceTest(
    private val projectRepository: ProjectRepository,
    private val service: ProjectSearchService,
) : BaseTest() {

    @BeforeEach
    fun beforeEach() {
        projectRepository.deleteAll()
    }

    @Test
    fun `search by name`() {
        createProject("Проект 1", "Описание проекта")
        val expectedId = createProject("МедиаНовостник", "---")
        createProject("Проект 2", "Описание проекта")

        service.searchProjects("новоси")
            .shouldHaveSize(1)
            .first()
            .id.shouldBe(expectedId)
    }

    @Test
    fun `search by description`() {
        createProject("Другой проект 1", "Текст проекта")
        val expectedId = createProject("--", "Проект в области кибербезопасности и защиты данных")
        createProject("Другой проект 2", "Текст проекта")

        service.searchProjects("область безопасности")
            .shouldHaveSize(1)
            .first()
            .id.shouldBe(expectedId)
    }

    @Test
    fun `search with mistakes`() {
        createProject("Другой проект 1", "Текст проекта")
        val expectedId = createProject("--", "Проект в области кибербезопасности и защиты данных")
        createProject("Другой проект 2", "Текст проекта")

        service.searchProjects("облость безо1асности")
            .shouldHaveSize(1)
            .first()
            .id.shouldBe(expectedId)
    }

    @RepeatedTest(20)
    fun `search with boost`() {
        createProject("Пустой", "Пустой")
        createProject("Проект", "Цель")
        createProject("Проект какойто там целевой", "Пустое поле")
        createProject("Проект", "Цель")
        createProject("Пустой", "Пустой")

        service.searchProjects("цель")
            .shouldHaveSize(3)
            .map { it.name + " - " + it.description }
            .shouldContainInOrder(
                "Проект какойто там целевой - Пустое поле",
                "Проект - Цель",
                "Проект - Цель",
            )
    }

    private fun createProject(name: String, description: String): String? {
        val project = projectRepository.save(
            Project(
                name = name,
                description = description
            )
        )
        return project.id
    }
}
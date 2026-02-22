package fyi.manpreet.moxie.projects

import fyi.manpreet.moxie.projects.samples.ButtonsShowcase
import fyi.manpreet.moxie.projects.samples.FeedbackShowcase
import fyi.manpreet.moxie.projects.samples.InputsShowcase

object ProjectRegistry {
    val all: List<ProjectDefinition> = listOf(
        ProjectDefinition(
            slug = "buttons-showcase",
            title = "Buttons Showcase",
            description = "Primary, secondary, ghost, and destructive action states.",
            content = { ButtonsShowcase() },
        ),
        ProjectDefinition(
            slug = "inputs-showcase",
            title = "Inputs Showcase",
            description = "Text input and checkbox behavior using UIKit components.",
            content = { InputsShowcase() },
        ),
        ProjectDefinition(
            slug = "feedback-showcase",
            title = "Feedback Showcase",
            description = "Progress and interactive slider feedback patterns.",
            content = { FeedbackShowcase() },
        ),
    )

    fun findBySlug(slug: String): ProjectDefinition? = all.firstOrNull { it.slug == slug }
}


package com.teamup.teamup_backend.config;

import com.teamup.teamup_backend.entity.Skill;
import com.teamup.teamup_backend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final SkillRepository skillRepository;

    private static final List<String> DEFAULT_SKILLS = List.of(
            "Java",
            "Spring Boot",
            "Python",
            "C++",
            "JavaScript",
            "TypeScript",
            "React",
            "Angular",
            "Vue.js",
            "HTML",
            "CSS",
            "Tailwind CSS",
            "Node.js",
            "Express.js",
            "Django",
            "FastAPI",
            "PostgreSQL",
            "MySQL",
            "MongoDB",
            "Redis",
            "Git",
            "GitHub",
            "Docker",
            "Kubernetes",
            "AWS",
            "REST API",
            "GraphQL",
            "Hibernate",
            "Spring Security",
            "JUnit",
            "Mockito",
            "Linux",
            "Firebase",
            "Machine Learning",
            "TensorFlow",
            "PyTorch",
            "OpenCV",
            "Android",
            "Flutter",
            "React Native",
            "Microservices"
    );

    @Override
    public void run(String... args) {

        if (skillRepository.count() > 0) {
            System.out.println("Skills already seeded.");
            return;
        }

        List<Skill> skills = DEFAULT_SKILLS
                .stream()
                .map(skillName -> Skill.builder()
                        .name(skillName)
                        .build())
                .toList();

        skillRepository.saveAll(skills);

        System.out.println("Successfully seeded " + skills.size() + " skills.");
    }
}
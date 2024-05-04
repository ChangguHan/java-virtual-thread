package org.example.mvc;

import org.example.mvc.MvcApplication.TestEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class MvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(MvcApplication.class, args);
    }

    @RequiredArgsConstructor
    @RestController
    public static class Controller {
        private final RestTemplate restTemplate = new RestTemplate();
        private final TestRepository testRepository;

        @Value("${remote.service.url}")
        private String remoteUrl;

        @GetMapping
        public TestEntity get(
                @RequestParam Long delay
        ) throws Exception{
            var req1 = restTemplate.getForEntity(remoteUrl + "?delay="+ delay, String.class);
            var req2 = restTemplate.getForEntity(remoteUrl + "?delay="+ delay, String.class);
            var entity = new TestEntity();
            entity.setText(req1.getBody() + req2.getBody());
            return testRepository.save(entity);
        }
    }

    @Setter
    @Entity
    public static class TestEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column
        private Long id;

        @Column(nullable = false)
        private String text;
    }
}

@Repository
interface TestRepository extends CrudRepository<TestEntity, Long> {

}

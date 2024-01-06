package at.wst.online_webshop.controller;

import at.wst.online_webshop.services.DBFiller;
import at.wst.online_webshop.services.NoSQLMigrationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private final DBFiller dbFiller;
    private final NoSQLMigrationService migrator;

    @RequestMapping("/version")
    public Long apiVersion() {
        return 1L;
    }

    @GetMapping("/test")
    public String test() {
        return "Test";
    }

    @GetMapping("/fill")
    public ResponseEntity<String> fillDB() {
        try {
            dbFiller.clearAndFillDB();
            return ResponseEntity.ok("DB filled");
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error filling database");
        }
    }

    @GetMapping("/migratedatabase")
    public void migrateDatabase() {
        logger.info("WE ARE MIGRATING");
        try {
            migrator.migrateToNoSQL();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package at.wst.online_webshop.controller;

import at.wst.online_webshop.services.DBFiller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {
    private final DBFiller dbFiller;

    @RequestMapping("/api/version")
    public Long apiVersion()   {
        return 1L;
    }

    @PostMapping("/fill")
    public String fillDB() {
        dbFiller.fillDB();
        return "DB filled";
    }

    /*@PostMapping("/migratedatabase")
    public void migrateDatabase() {
        migrator.migrateDatabase();
    }*/

}

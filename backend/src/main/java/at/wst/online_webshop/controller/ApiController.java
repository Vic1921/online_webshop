package at.wst.online_webshop.controller;

import at.wst.online_webshop.services.DBFiller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {
    private final DBFiller dbFiller;

    @RequestMapping("/version")
    public Long apiVersion()   {
        return 1L;
    }

    @GetMapping("/test")
    public String test() {
        return "Test";
    }

    @GetMapping("/fill")
    public String fillDB() {
        dbFiller.clearAndFillDB();
        return "DB filled";
    }

    /*@GetMapping("/migratedatabase")
    public void migrateDatabase() {
        migrator.migrateDatabase();
    }*/

}

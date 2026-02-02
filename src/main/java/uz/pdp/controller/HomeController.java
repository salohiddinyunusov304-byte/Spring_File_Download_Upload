package uz.pdp.controller;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;
import java.util.Objects;

@Controller
public class HomeController {
    private final MessageSource messageSource;

    public HomeController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/home")
    public String home(@CookieValue(name = "language", required = false) String cookieLang,  @RequestParam(name = "lang", required = false) String language) {
        String lang = Objects.requireNonNullElse(language, cookieLang != null ? cookieLang : "uz");

        String message = messageSource.getMessage(
                "welcome", // key
                null, //
                Locale.forLanguageTag(lang) // language
        );

        String message2 = messageSource.getMessage(
                "welcome2", // key
                new Object[]{"Umar"}, //
                Locale.forLanguageTag(lang) // language
        );

        String message3 = messageSource.getMessage(
                "welcome3", // key
                new Object[]{"Umar", "Usmon", "Ali"}, //
                Locale.forLanguageTag(lang) // language
        );

        System.out.println("message: " + message);
        System.out.println("message2: " + message2);
        System.out.println("message3: " + message3);
        return "home";
    }
}

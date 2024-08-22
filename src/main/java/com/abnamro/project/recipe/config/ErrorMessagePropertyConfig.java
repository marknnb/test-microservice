package com.abnamro.project.recipe.config;

import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ErrorMessagePropertyConfig {
    private final MessageSource messageSource;

    @Autowired
    public ErrorMessagePropertyConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageCode) {
        return getMessage(messageCode, null);
    }

    public String getMessage(String messageCode, List<Object> args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, args != null ? args.toArray() : null, locale);
    }
}

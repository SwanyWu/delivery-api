package com.ahold.ctp.assignment.controller

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        // Redirect root URL to swagger-ui/index.html
        registry.addViewController("/").setViewName("redirect:/swagger-ui/index.html")
    }
}
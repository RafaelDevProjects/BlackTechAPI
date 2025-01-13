package com.blacktech.api.domain.event;

import org.springframework.web.multipart.MultipartFile;


public record EventRequestDTO(String title, String description, Long date, String city, String state, Boolean remote, String eventUrl, MultipartFile image) {
}

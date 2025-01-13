package com.blacktech.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.blacktech.api.domain.coupon.Coupon;
import com.blacktech.api.domain.event.Event;
import com.blacktech.api.domain.event.EventDetailsDTO;
import com.blacktech.api.domain.event.EventRequestDTO;
import com.blacktech.api.domain.event.EventResponseDTO;
import com.blacktech.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private AmazonS3 s3client;

    @Autowired
    private EventRepository repository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CouponServise couponServise;

    @Value("${aws.bucket.name}")
    private String bucketName;


    public Event createEvent(EventRequestDTO data) {
        String imgUrl = null;

        if (data.image() != null) {
            imgUrl = this.uploadImg(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setDescription(data.description());
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());
        repository.save(newEvent);

        if (!data.remote()) {
            this.addressService.createAddress(data, newEvent);
        }


        return newEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = this.repository.findUpComingEvents(new Date(), pageable);
        return eventsPage.map(event -> new EventResponseDTO(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getAddress() != null ? event.getAddress().getCity() : "",
                    event.getAddress() != null ? event.getAddress().getUf() : "",
                    event.getRemote(),
                    event.getEventUrl(),
                    event.getImgUrl())
                )
                .stream()
                .toList();
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf, Date startDate, Date endDate) {
        title = (title != null) ? title : "";
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : new Date();
        if (endDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, 40); // Adiciona 10 anos
            endDate = calendar.getTime();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = this.repository.findFilteredEvents(title, city, uf, startDate, endDate, pageable);
        return eventsPage.map(event -> new EventResponseDTO(
                    event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .stream()
                .toList();
    }

    public EventDetailsDTO getEventDetails(UUID eventId){
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not Found"));

        List<Coupon> coupons = couponServise.consultCoupons(eventId, new Date());

        List<EventDetailsDTO.CouponDTO> couponDTOs = coupons.stream()
                .map(coupon -> new EventDetailsDTO.CouponDTO(
                        coupon.getCode(),
                        coupon.getDiscount(),
                        coupon.getValid()))
                .collect(Collectors.toList());

        return new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getImgUrl(),
                event.getEventUrl(),
                couponDTOs);
    }

    private String uploadImg(MultipartFile multipartFile){
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.convertMultipartToFile(multipartFile);
            s3client.putObject(bucketName,fileName, file);
            file.delete();
            return s3client.getUrl(bucketName, fileName).toString();
        } catch (Exception e){
            System.out.println("erro ao subir arquivo");
            System.out.println(e.toString());

            return null;
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }


}

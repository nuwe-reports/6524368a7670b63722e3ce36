package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(){
        List<Appointment> appointments = new ArrayList<>();

        appointmentRepository.findAll().forEach(appointments::add);

        if (appointments.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id){
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()){
            return new ResponseEntity<>(appointment.get(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/appointment")
    public ResponseEntity<List<Appointment>> createAppointment(@RequestBody Appointment appointment) {
        if (isInvalidRequest(appointment)) {
            return ResponseEntity.badRequest().build();
        }

        List<Appointment> appointments = appointmentRepository.findAll();
        if (isOverlapping(appointments, appointment)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        appointmentRepository.save(appointment);
        appointments.add(appointment);

        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id){

        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
        
    }

    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments(){
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isOverlapping(List<Appointment> appointments, Appointment appointment) {
        return appointments.stream().anyMatch(appointment::overlaps);
    }

    private boolean isInvalidRequest(Appointment appointment) {
        return isAnyFieldNull(appointment) || !isFinishAfterStart(appointment);
    }

    private boolean isAnyFieldNull(Appointment appointment) {
        return  appointment == null ||
                appointment.getPatient() == null ||
                appointment.getDoctor() == null ||
                appointment.getRoom() == null ||
                appointment.getStartsAt() == null ||
                appointment.getFinishesAt() == null;
    }

    private boolean isFinishAfterStart(Appointment appointment) {
        return appointment.getFinishesAt().isAfter(appointment.getStartsAt());
    }

}

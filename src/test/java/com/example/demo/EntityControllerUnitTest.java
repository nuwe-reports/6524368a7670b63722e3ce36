
package com.example.demo;

import com.example.demo.controllers.DoctorController;
import com.example.demo.controllers.PatientController;
import com.example.demo.controllers.RoomController;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest{

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String DOCTORS_URL = "/api/doctors";
    private static final String DOCTOR_URL = "/api/doctor";


    @Test
    void shouldGetDoctors() throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor ("Jose Luis", "Olaya", 37, "j.olaya@hospital.accwe"));
        doctors.add(new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe"));

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(MockMvcRequestBuilders.get(DOCTORS_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(doctors)));

        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void shouldGetDoctorsNoContent() throws Exception {
        List<Doctor> doctors = new ArrayList<>();

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(MockMvcRequestBuilders.get(DOCTORS_URL))
                .andExpect(status().isNoContent());

        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void shouldGetDoctorById() throws Exception {
        Optional<Doctor> doctor = Optional.of(new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe"));
        when(doctorRepository.findById(anyLong())).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders.get(DOCTORS_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(doctor.get())));

        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldReturnDoctorNotFount() throws Exception {
        Optional<Doctor> doctor = Optional.empty();
        when(doctorRepository.findById(anyLong())).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders.get(DOCTORS_URL + "/1"))
                .andExpect(status().isNotFound());

        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldCreateDoctor() throws Exception {
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders.post(DOCTOR_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(doctor)));

        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void shouldDeleteExistingDoctor() throws Exception {
        Optional<Doctor> doctor = Optional.of(new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe"));

        when(doctorRepository.findById(anyLong())).thenReturn(doctor);
        doNothing().when(doctorRepository).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(DOCTORS_URL + "/1"))
                .andExpect(status().isOk());

        verify(doctorRepository, times(1)).findById(anyLong());
        verify(doctorRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldTryDeleteNotExistingDoctor() throws Exception {
        Optional<Doctor> doctor = Optional.empty();

        when(doctorRepository.findById(anyLong())).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders.delete(DOCTORS_URL + "/1"))
                .andExpect(status().isNotFound());

        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldDeleteAllDoctors() throws Exception {
        doNothing().when(doctorRepository).deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.delete(DOCTORS_URL))
                .andExpect(status().isOk());

        verify(doctorRepository, times(1)).deleteAll();
    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest{

    @MockBean
    private PatientRepository patientRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PATIENTS_URL = "/api/patients";
    private static final String PATIENT_URL = "/api/patient";

    @Test
    void shouldGetPatients() throws Exception {
        List<Patient> patients = new ArrayList<>();
        Patient patient1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Paulino", "Antunez", 37, "p.antunez@email.com");

        patients.add(patient1);
        patients.add(patient2);

        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(MockMvcRequestBuilders.get(PATIENTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patients)));

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void shouldGetPatientsNoContent() throws Exception {
        List<Patient> patients = new ArrayList<>();
        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(MockMvcRequestBuilders.get(PATIENTS_URL))
                .andExpect(status().isNoContent());

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void shouldGetPatientById() throws Exception {
        Optional<Patient> patient = Optional.of(new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"));

        when(patientRepository.findById(anyLong())).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.get(PATIENTS_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patient.get())));

        verify(patientRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldGetPatientNotFound() throws Exception {
        Optional<Patient> patient = Optional.empty();
        when(patientRepository.findById(anyLong())).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.get(PATIENTS_URL + "/1"))
                .andExpect(status().isNotFound());

        verify(patientRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldCreatePatient() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.post(PATIENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());

        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void shouldDeleteExistingPatient() throws Exception {
        Optional<Patient> patient = Optional.of(new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"));

        when(patientRepository.findById(anyLong())).thenReturn(patient);
        doNothing().when(patientRepository).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(PATIENTS_URL + "/1"))
                .andExpect(status().isOk());

        verify(patientRepository, times(1)).findById(anyLong());
        verify(patientRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldTryDeleteNotExistingPatient() throws Exception {
        Optional<Patient> patient = Optional.empty();

        when(patientRepository.findById(anyLong())).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.delete(PATIENTS_URL + "/1"))
                .andExpect(status().isNotFound());

        verify(patientRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldDeleteAllPatients() throws Exception {
        doNothing().when(patientRepository).deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.delete(PATIENTS_URL))
                .andExpect(status().isOk());

        verify(patientRepository, times(1)).deleteAll();
    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest{

    @MockBean
    private RoomRepository roomRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ROOMS_URL = "/api/rooms";
    private static final String ROOM_URL = "/api/room";

    @Test
    void shouldGetRooms() throws Exception {
        List<Room> rooms = new ArrayList<>();
        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Oncology");

        rooms.add(room1);
        rooms.add(room2);

        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(MockMvcRequestBuilders.get(ROOMS_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rooms)));

        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void shouldGetRoomsNoContent() throws Exception {
        List<Room> rooms = new ArrayList<>();
        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(MockMvcRequestBuilders.get(ROOMS_URL))
                .andExpect(status().isNoContent());

        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void shouldGetPatientByRoomName() throws Exception {
        Optional<Room> room = Optional.of(new Room("Dermatology"));

        when(roomRepository.findByRoomName(anyString())).thenReturn(room);

        mockMvc.perform(MockMvcRequestBuilders.get(ROOMS_URL + "/Dermatology"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(room.get())));

        verify(roomRepository, times(1)).findByRoomName(anyString());
    }

    @Test
    void shouldGetRoomNotFound() throws Exception {
        Optional<Room> room = Optional.empty();
        when(roomRepository.findByRoomName(anyString())).thenReturn(room);

        mockMvc.perform(MockMvcRequestBuilders.get(ROOMS_URL + "/Dermatology"))
                .andExpect(status().isNotFound());

        verify(roomRepository, times(1)).findByRoomName(anyString());
    }

    @Test
    void shouldCreateRoom() throws Exception {
        Room room = new Room("Dermatology");
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        mockMvc.perform(MockMvcRequestBuilders.post(ROOM_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void shouldDeleteExistingRoom() throws Exception {
        Optional<Room> room = Optional.of(new Room("Dermatology"));

        when(roomRepository.findByRoomName(anyString())).thenReturn(room);
        doNothing().when(roomRepository).deleteByRoomName(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete(ROOMS_URL + "/Dermatology"))
                .andExpect(status().isOk());

        verify(roomRepository, times(1)).findByRoomName(anyString());
        verify(roomRepository, times(1)).deleteByRoomName(anyString());
    }

    @Test
    void shouldTryDeleteNotExistingRoom() throws Exception {
        Optional<Room> room = Optional.empty();

        when(roomRepository.findById(anyLong())).thenReturn(room);

        mockMvc.perform(MockMvcRequestBuilders.delete(ROOMS_URL + "/Dermatology"))
                .andExpect(status().isNotFound());

        verify(roomRepository, times(1)).findByRoomName(anyString());
    }

    @Test
    void shouldDeleteAllRooms() throws Exception {
        doNothing().when(roomRepository).deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.delete(ROOMS_URL))
                .andExpect(status().isOk());

        verify(roomRepository, times(1)).deleteAll();
    }
}

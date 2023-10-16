package com.example.demo;

import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

	private Doctor d1;

	private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    @BeforeEach
    void beforeEach() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        d1 = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        r1 = new Room("Dermatology");
        a1 = new Appointment(p1, d1, r1, LocalDateTime.parse("19:30 24/04/2023", formatter),  LocalDateTime.parse("19:30 24/04/2023", formatter));
        a2 = new Appointment(p1, d1, r1, LocalDateTime.parse("19:30 25/04/2023", formatter),  LocalDateTime.parse("19:30 25/04/2023", formatter));
        a3 = new Appointment(p1, d1, r1, LocalDateTime.parse("19:30 26/04/2023", formatter),  LocalDateTime.parse("19:30 26/04/2023", formatter));
    }

    @Test
    void shouldTestDoctorEntityMapping() {
        entityManager.getEntityManager().persist(d1);

        String query = "SELECT * FROM doctors";
        List<Doctor> doctors = entityManager.getEntityManager()
                .createNativeQuery(query, Doctor.class)
                .getResultList();

        assertEquals(1, doctors.size());
        assertEquals(doctors.get(0).getId(), d1.getId());
        assertEquals(doctors.get(0).getFirstName(), d1.getFirstName());
        assertEquals(doctors.get(0).getLastName(), d1.getLastName());
        assertEquals(doctors.get(0).getAge(), d1.getAge());
        assertEquals(doctors.get(0).getEmail(), d1.getEmail());
    }

    @Test
    void shouldTestPatientEntityMapping() {
        entityManager.getEntityManager().persist(p1);
        final String query = "SELECT * FROM patient";
        List<Patient> patients = entityManager.getEntityManager()
                .createNativeQuery(query, Patient.class)
                .getResultList();

        assertEquals(1, patients.size());
        assertEquals(patients.get(0).getId(), p1.getId());
        assertEquals(patients.get(0).getFirstName(), p1.getFirstName());
        assertEquals(patients.get(0).getFirstName(), p1.getFirstName());
        assertEquals(patients.get(0).getLastName(), p1.getLastName());
        assertEquals(patients.get(0).getAge(), p1.getAge());
        assertEquals(patients.get(0).getEmail(), p1.getEmail());
    }

    @Test
    void shouldTestRoomEntityMapping() {
        entityManager.getEntityManager().persist(r1);
        final String query = "SELECT * FROM room";
        List<Room> rooms = entityManager.getEntityManager()
                .createNativeQuery(query, Room.class)
                .getResultList();

        assertEquals(1, rooms.size());
        assertEquals(rooms.get(0).getRoomName(), r1.getRoomName());
    }

    @Test
    void shouldTestAppointmentEntityMapping() {
        entityManager.getEntityManager().persist(a1);
        entityManager.getEntityManager().persist(a2);
        entityManager.getEntityManager().persist(a3);

        final String query = "SELECT * FROM Appointment";
        List<Appointment> appointments = entityManager.getEntityManager()
                .createNativeQuery(query, Appointment.class)
                .getResultList();

        assertEquals(3, appointments.size());
        assertEquals(a1.getPatient(), p1);
        assertEquals(a1.getDoctor(), d1);
        assertEquals(a1.getRoom(), r1);
        assertEquals(a2.getPatient(), p1);
        assertEquals(a2.getDoctor(), d1);
        assertEquals(a2.getRoom(), r1);
        assertEquals(a3.getPatient(), p1);
        assertEquals(a3.getDoctor(), d1);
        assertEquals(a3.getRoom(), r1);
    }
}

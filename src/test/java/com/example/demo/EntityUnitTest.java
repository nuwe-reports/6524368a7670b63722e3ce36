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
        entityManager.getEntityManager().flush();

        String query = "SELECT * FROM doctors";
        List<Doctor> doctors = entityManager.getEntityManager()
                .createNativeQuery(query, Doctor.class)
                .getResultList();

        assertEquals(1, doctors.size());
        assertEquals(doctors.get(0), d1);
    }

    @Test
    void shouldTestPatientEntityMapping() {
        entityManager.getEntityManager().persist(p1);
        entityManager.getEntityManager().flush();

        final String query = "SELECT * FROM patient";
        List<Patient> patients = entityManager.getEntityManager()
                .createNativeQuery(query, Patient.class)
                .getResultList();

        assertEquals(1, patients.size());
        assertEquals(patients.get(0), p1);
    }

    @Test
    void shouldTestRoomEntityMapping() {
        entityManager.getEntityManager().persist(r1);
        entityManager.getEntityManager().flush();

        final String query = "SELECT * FROM room";
        List<Room> rooms = entityManager.getEntityManager()
                .createNativeQuery(query, Room.class)
                .getResultList();

        assertEquals(1, rooms.size());
        assertEquals(rooms.get(0), r1);
    }

    @Test
    void shouldTestAppointmentEntityMapping() {
        entityManager.getEntityManager().persist(a1);
        entityManager.getEntityManager().persist(a2);
        entityManager.getEntityManager().persist(a3);

        entityManager.getEntityManager().flush();


        final String query = "SELECT * FROM Appointment";
        List<Appointment> appointments = entityManager.getEntityManager()
                .createNativeQuery(query, Appointment.class)
                .getResultList();

        assertEquals(3, appointments.size());
        assertEquals(appointments.get(0), a1);
        assertEquals(appointments.get(1), a2);
        assertEquals(appointments.get(2), a3);

    }
}

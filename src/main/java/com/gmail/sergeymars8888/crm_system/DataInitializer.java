package com.gmail.sergeymars8888.crm_system;

import com.gmail.sergeymars8888.crm_system.model.Client;
import com.gmail.sergeymars8888.crm_system.model.Contact;
import com.gmail.sergeymars8888.crm_system.model.Task;
import com.gmail.sergeymars8888.crm_system.model.TaskStatus;
import com.gmail.sergeymars8888.crm_system.repository.ClientRepository;
import com.gmail.sergeymars8888.crm_system.repository.ContactRepository;
import com.gmail.sergeymars8888.crm_system.repository.TaskRepository;
import com.gmail.sergeymars8888.crm_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void run(String... args) {
        Client client1 = new Client(null, "Tech Solutions", "IT", "123 Tech St, Silicon Valley", null);
        Client client2 = new Client(null, "Global Retailers", "Retail", "456 Retail Ave, New York", null);
        Client client3 = new Client(null, "EcoFarm", "Agriculture", "789 Greenfield Rd, Oregon", null);
        Client client4 = new Client(null, "Banking Corp", "Finance", "101 Bank St, London", null);
        Client client5 = new Client(null, "Health Innovations", "Healthcare", "202 Wellness Blvd, LA", null);

        clientRepository.saveAll(Arrays.asList(client1, client2, client3, client4, client5));

        Contact contact1 = new Contact(null, "John", "Doe", "john.doe@techsolutions.com", "123-456-7890", client1);
        Contact contact2 = new Contact(null, "Jane", "Smith", "jane.smith@techsolutions.com", "987-654-3210", client1);
        Contact contact3 = new Contact(null, "Alice", "Johnson", "alice.johnson@globalretailers.com", "456-789-0123", client2);
        Contact contact4 = new Contact(null, "Bob", "Lee", "bob.lee@globalretailers.com", "321-654-9870", client2);
        Contact contact5 = new Contact(null, "Sara", "Williams", "sara.williams@ecofarm.com", "654-321-0987", client3);
        Contact contact6 = new Contact(null, "Tom", "Wilson", "tom.wilson@ecofarm.com", "555-123-4567", client3);
        Contact contact7 = new Contact(null, "Emily", "Green", "emily.green@bankingcorp.com", "987-321-8765", client4);
        Contact contact8 = new Contact(null, "Michael", "Brown", "michael.brown@bankingcorp.com", "654-987-3210", client4);
        Contact contact9 = new Contact(null, "Olivia", "Davis", "olivia.davis@healthinnovations.com", "987-654-4321", client5);
        Contact contact10 = new Contact(null, "Jack", "Miller", "jack.miller@healthinnovations.com", "321-987-6543", client5);

        contactRepository.saveAll(Arrays.asList(contact1, contact2, contact3, contact4, contact5, contact6, contact7, contact8, contact9, contact10));

        Task task1 = new Task(null, "Website redesign for Tech Solutions", TaskStatus.OPEN, LocalDateTime.now().plusDays(5), contact1);
        Task task2 = new Task(null, "New marketing campaign for Global Retailers", TaskStatus.IN_PROGRESS, LocalDateTime.now().plusDays(3), contact3);
        Task task3 = new Task(null, "Farm equipment maintenance", TaskStatus.OPEN, LocalDateTime.now().plusDays(7), contact5);
        Task task4 = new Task(null, "Compliance audit for Banking Corp", TaskStatus.COMPLETED, LocalDateTime.now().minusDays(1), contact7);
        Task task5 = new Task(null, "Develop new healthcare app", TaskStatus.OPEN, LocalDateTime.now().plusDays(10), contact9);

        Task task6 = new Task(null, "Consulting for IT infrastructure", TaskStatus.IN_PROGRESS, LocalDateTime.now().plusHours(12), contact1);
        Task task7 = new Task(null, "Store renovation for Global Retailers", TaskStatus.COMPLETED, LocalDateTime.now().plusDays(15), contact4);
        Task task8 = new Task(null, "Soil testing for EcoFarm", TaskStatus.COMPLETED, LocalDateTime.now().minusDays(5), contact6);
        Task task9 = new Task(null, "Annual financial report for Banking Corp", TaskStatus.OPEN, LocalDateTime.now().plusDays(20), contact8);
        Task task10 = new Task(null, "Launch new fitness platform for Health Innovations", TaskStatus.IN_PROGRESS, LocalDateTime.now().plusDays(2), contact10);

        taskRepository.saveAll(Arrays.asList(task1, task2, task3, task4, task5, task6, task7, task8, task9, task10));


        System.out.println("Test data has been loaded successfully!");
    }
}

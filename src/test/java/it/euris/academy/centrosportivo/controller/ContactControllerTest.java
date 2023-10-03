package it.euris.academy.centrosportivo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.academy.centrosportivo.entity.Contact;
import it.euris.academy.centrosportivo.entity.Customer;
import it.euris.academy.centrosportivo.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetOneContact() throws Exception {

        Long id = 1L;

        Contact contact = Contact
                .builder()
                .id(id)
                .build();

        List<Contact> contacts = List.of(contact);

        when(contactService.findAll()).thenReturn(contacts);

        mockMvc.perform(MockMvcRequestBuilders.get("/contacts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id))
        ;
    }

    @Test
    void shouldGetContactById() throws Exception {

        Long id = 1L;

        Contact contact = Contact
                .builder()
                .id(id)
                .build();


        when(contactService.findById(id)).thenReturn(contact);

        mockMvc.perform(MockMvcRequestBuilders.get("/contacts/{id}", id)).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(id))
        ;
    }

    @Test
    void shouldInsertAContact() throws Exception {

        Long id = 1L;

        Contact contact = Contact
                .builder()
                .id(id)
                .build();

        when(contactService.save(any())).thenReturn(contact);

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(id))
        ;
    }

    @Test
    void shouldGetContactByCustomerId() throws Exception {

        String firstName = "Mario";
        String lastName = "Rossi";
        Long idCustomer = 1L;
        String taxcode = "1234";

        Customer customer = Customer
                .builder()
                .id(idCustomer)
                .name(firstName)
                .surname(lastName)
                .tax_code(taxcode)
                .build();


        Long id = 1L;

        Contact contact = Contact
                .builder()
                .id(id)
                .customer(customer)
                .build();

        List<Contact> contacts = List.of(contact);

        when(contactService.findAll()).thenReturn(contacts);


        mockMvc.perform(MockMvcRequestBuilders.get("/contacts/customer/{id}", idCustomer)).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
        ;
    }


}
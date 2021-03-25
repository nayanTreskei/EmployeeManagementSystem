package com.vaadin.nayan.ems.backend.service;

import org.springframework.stereotype.Service;

import com.vaadin.nayan.ems.backend.entity.Company;
import com.vaadin.nayan.ems.backend.entity.Contact;
import com.vaadin.nayan.ems.backend.repository.CompanyRepository;
import com.vaadin.nayan.ems.backend.repository.ContactRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContactService {
    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());
    private ContactRepository contactRepository;
    private CompanyRepository companyRepository;

    public ContactService(ContactRepository contactRepository,
                          CompanyRepository companyRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public List<Contact> findAll(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
            return contactRepository.findAll();
        } else  {
            return  contactRepository.search(filterText);
        }
    }

    public long count() {
        return contactRepository.count();
    }

    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }

    public void save(Contact contact) {
        if (contact == null) {
            LOGGER.log(Level.SEVERE,
                "Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

    @PostConstruct
    public void populateTestData() {
        if (companyRepository.count() == 0) {
            companyRepository.saveAll(
                Stream.of("Metquay","Treskei","MNC","Accenture","Tech Mahindra","Cognizant","TCS","Concentrix")
                    .map(Company::new)
                    .collect(Collectors.toList()));
        }

        if (contactRepository.count() == 0) {
            Random r = new Random(0);
            List<Company> companies = companyRepository.findAll();
            contactRepository.saveAll(
                Stream.of("CID1 c1", "CID2 c2", "CID3 c3",
                    "K1 J1", "A3 M32", "A23 K32", "Y12 G21", "H2 S33",
                    "E45 S23", "D12 D23", "R1 D32", "Y21 J21", "K1 G2",
                "W23 W12", "C4 C34", "I23 C33", "Q2 H23", "M1 S2",
                    "B34 N54")
                    .map(name -> {
                        String[] split = name.split(" ");
                        Contact contact = new Contact();
                        contact.setFirstName(split[0]);
                        contact.setLastName(split[1]);
                        contact.setCompany(companies.get(r.nextInt(companies.size())));
                        contact.setStatus(Contact.Status.values()[r.nextInt(Contact.Status.values().length)]);
                        String email = (contact.getFirstName() + "." + contact.getLastName() + "@" + contact.getCompany().getName().replaceAll("[\\s-]", "") + ".com").toLowerCase();
                        contact.setEmail(email);
                        return contact;
                    }).collect(Collectors.toList()));
        }
    }
}

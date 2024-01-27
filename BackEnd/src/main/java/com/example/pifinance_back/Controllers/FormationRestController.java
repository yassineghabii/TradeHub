package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.Client;
import com.example.pifinance_back.Entities.Formation;
import com.example.pifinance_back.Repositories.FormationRepository;
import com.example.pifinance_back.Services.EmailSenderService;
import com.example.pifinance_back.Services.FormationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/formation")
@CrossOrigin(origins = "http://localhost:4200")
public class FormationRestController {
    private FormationService formationService;
    private FormationRepository formationRepository;
    @Autowired
    private EmailSenderService service;

    @GetMapping("/all")
    List<Formation> retrieveAllFormation() {
        return formationService.retrieveAllFormation();
    }

    @PostMapping("/add")
    Formation AddFormation(@RequestBody Formation formation) {
        return formationService.addFormation(formation);
    }

    @DeleteMapping("/delete/{id}")
    void removeFormation(@PathVariable("id") Integer idFormation) {
        formationService.removeFormation(idFormation);
    }

    @GetMapping("/get/{id}")
    Formation retrieveFormation(@PathVariable("id") Integer idFormation) {
        return formationService.retrieveFormation(idFormation);
    }

    @PutMapping("/update")
    Formation updateFormation(@RequestBody Formation formation) {
        return formationService.updateFormation(formation);
    }

    @GetMapping("/betweendate/{date_debut}/{date_fin}")
    List<Formation> retrieveFormationdatebetween(@PathVariable("date_debut") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate datedebut, @PathVariable("date_fin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate datefin) {
        return formationService.findByDateBetween(datedebut, datefin);
    }

    @GetMapping("/orderbydate")
    List<Formation> retrieveFormationbydate() {
        return formationService.findByOrderByDate_debutAsc();
    }

    @GetMapping("/cancel/{eventid}")
    public ResponseEntity<String> cancelEvent(@PathVariable("eventid") Integer eventId) throws IOException, MessagingException {
        Optional<Formation> optionalEvent = formationRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Formation formation = optionalEvent.get();
            formationRepository.deleteById(eventId);
            File file = ResourceUtils.getFile("src/main/java/com/example/pifinance_back/eventcancel.html");
            System.out.println("File Found : " + file.exists());
            String content = new String(Files.readAllBytes(file.toPath()));
            content = content.replace("${event}", formation.getNom());
            Set<Client> clientsList = formation.getClients();
            for (Client client : clientsList) {
                service.sendSimpleEmail(client.getEmail(), content, "event canceled ! " + formation.getNom());
                return ResponseEntity.ok("Event has been cancelled successfully");
            }
        } else {
            throw new ResourceNotFoundException("Event not found with id " + eventId);
        }
        return null ;
    }
}

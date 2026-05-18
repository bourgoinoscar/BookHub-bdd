package com.example.backend.Config;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Enum.StatutResa;
import org.hibernate.type.descriptor.java.LocalDateJavaType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    private final IRoleRepository roleRepo;
    private final IUtilisateurRepository userRepo;
    private final ILivreRepository livreRepo;
    private final IEmpruntRepository empruntRepo;
    private final IReservationRepository resaRepo;
    private final IRevueRepository revueRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(IRoleRepository roleRepo, IUtilisateurRepository userRepo,
                           ILivreRepository livreRepo, IEmpruntRepository empruntRepo,
                           IReservationRepository resaRepo, IRevueRepository revueRepo, PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.livreRepo = livreRepo;
        this.empruntRepo = empruntRepo;
        this.resaRepo = resaRepo;
        this.revueRepo = revueRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. NE REMPLIR QUE SI LA BASE EST VIDE
        if (roleRepo.count() > 0) return;

        System.out.println("--- Démarrage du remplissage de la base de données ---");

        // 2. CRÉATION DES RÔLES
        Role adminRole = roleRepo.save(new Role(null, "ADMIN", null));
        Role biblioRole = roleRepo.save(new Role(null, "BIBLIOTHECAIRE", null));
        Role lecteurRole = roleRepo.save(new Role(null, "LECTEUR", null));

        // 3. CRÉATION DES UTILISATEURS
        Utilisateur admin = userRepo.save(new Utilisateur(null, "Boss", "Hugo", LocalDate.of(2000,12,31), "0601020304", "admin@test.com", passwordEncoder.encode("pass"), adminRole, null));
        Utilisateur biblio = userRepo.save(new Utilisateur(null, "Curie", "Marie", LocalDate.of(1997,10,02), "0611223344", "marie@biblio.com", passwordEncoder.encode("azerty"), biblioRole, null));
        Utilisateur lecteur1 = userRepo.save(new Utilisateur(null, "Dupont", "Jean", LocalDate.of(1968,06,15), "0677889900", "jean@mail.com", passwordEncoder.encode("chaton"), lecteurRole, null));
        Utilisateur lecteur2 = userRepo.save(new Utilisateur(null, "Smith", "Jane", LocalDate.of(2010,01,4), "0655443322", "jane@mail.com", passwordEncoder.encode("1234"), lecteurRole, null));

        // 4. CRÉATION DES LIVRES (Scénarios variés)
        Livre l1 = livreRepo.save(new Livre(null, "Le Seigneur des Anneaux", "Tolkien", "Fantasy", "Un anneau magique...", "ISBN001", 5, LocalDate.now(), null));
        Livre l2 = livreRepo.save(new Livre(null, "1984", "George Orwell", "Dystopie", "Big Brother watching...", "ISBN002", 0, LocalDate.now(), null)); // STOCK VIDE
        Livre l3 = livreRepo.save(new Livre(null, "Le Petit Prince", "St-Exupéry", "Conte", "Dessine-moi un mouton", "ISBN003", 2, LocalDate.now(), null));
        Livre l4 = livreRepo.save(new Livre(null, "Clean Code", "Robert Martin", "Tech", "Apprendre à coder propre", "ISBN004", 1, LocalDate.now(), null));
        Livre l5 = livreRepo.save(new Livre(null, "2024", "George Orwell", "Conte", "Dessine-moi un futur", "ISBN005", 2, LocalDate.now(), null));
        Livre l6 = livreRepo.save(new Livre(null, "La Servante écarlate", "Margaret Atwood", "Dystopie", "Dans une société du futur, la République de Gilead, les femmes ont perdu tous leurs droits et sont entièrement soumises aux hommes....", "ISBN006", 2, LocalDate.now(), null));

        // 5. CRÉATION DES EMPRUNTS (Test des dates et retards)
        // Emprunt terminé (rendu à l'heure)
        empruntRepo.save(new Emprunt(null, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6), LocalDate.now().minusDays(7), lecteur1, l1));

        // Emprunt en cours (OK)
        empruntRepo.save(new Emprunt(null, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9), null, lecteur1, l3));

        // Emprunt EN RETARD (Date retour prévu dépassée, pas de date de retour effectif)
        empruntRepo.save(new Emprunt(null, LocalDate.now().minusDays(30), LocalDate.now().minusDays(15), null, lecteur2, l4));

        // 6. CRÉATION DES RÉSERVATIONS
        // Jean réserve "1984" car le stock est à 0
        resaRepo.save(new Reservation(null, StatutResa.EN_ATTENTE, lecteur1, l2));

        // 7. CRÉATION DES REVUES (Notes et coms)
        revueRepo.save(new Revue(null, 5, "Incroyable, un classique !", LocalDate.now(), lecteur1, l1));
        revueRepo.save(new Revue(null, 4, "Un peu flippant mais nécessaire.", LocalDate.now(), lecteur2, l2));

        System.out.println("--- Base de données prête pour les tests ! ---");
    }
}
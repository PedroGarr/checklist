package pedrogarr.checklist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

 //Lombok data is making the Setters and Getters
@Data
@Entity(name = "tb_users")
public class UserModel {
    
    @Id //PRIMARY KEY
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column (unique = true)
    private String username;
    private String name;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    
}

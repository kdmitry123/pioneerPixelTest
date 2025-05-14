package by.pioneerpixeltest.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "email_data")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "userId")
public class EmailData {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, length = 200, unique = true)
    @NotBlank(message = "Email не может быть пустым")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9+_.-]*@[A-Za-z0-9.-]+$", message = "Email должен быть в корректном формате и начинаться с буквы или цифры")
    private String email;
}
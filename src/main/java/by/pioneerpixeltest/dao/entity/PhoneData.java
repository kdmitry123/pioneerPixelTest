package by.pioneerpixeltest.dao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "phone_data")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "user")
public class PhoneData {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 13, unique = true)
    @Size(max = 13, message = "Длина номера телефона не должна превышать 13 символов")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Номер телефона должен содержать только цифры и не начинаться с нуля")
    @NotBlank
    private String phone;
}

package by.pioneerpixeltest.dao.entity;

import jakarta.persistence.*;
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
@ToString(exclude = "userId")
public class PhoneData {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, length = 13, unique = true)
    private String phone;
}

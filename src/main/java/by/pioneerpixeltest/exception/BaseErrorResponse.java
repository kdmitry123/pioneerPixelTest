package by.pioneerpixeltest.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BaseErrorResponse implements Serializable {
    public ZonedDateTime timestamp;
    public int status;
    public String error;
    public String message;
}

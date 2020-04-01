package com.space4u.mpkgen.api;

import com.space4u.mpkgen.util.validation.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "matchingPassword", message = "nie może się różnić")
})
@Getter
@Setter
public class CrmUser {

    private int userId;

    @NotNull(message = "jest polem obowiązkowym")
    @Size(min = 1, message = "jest polem obowiązkowym")
    private String userName;

    @NotNull(message = "jest polem obowiązkowym")
    @Size(min = 1, message = "jest polem obowiązkowym")
    private String password;

    @NotNull(message = "jest polem obowiązkowym")
    @Size(min = 1, message = "jest polem obowiązkowym")
    private String matchingPassword;

    @NotNull(message = "jest polem obowiązkowym")
    @Size(min = 1, message = "jest polem obowiązkowym")
    private String firstName;

    @NotNull(message = "jest polem obowiązkowym")
    @Size(min = 1, message = "jest polem obowiązkowym")
    private String lastName;

//    @NotNull(message = "jest polem obowiązkowym")
//    @Size(min = 1, message = "jest polem obowiązkowym")
    private String roles;

}
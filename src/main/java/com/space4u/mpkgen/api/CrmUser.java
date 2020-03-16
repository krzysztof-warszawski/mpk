package com.space4u.mpkgen.api;

import com.space4u.mpkgen.util.validation.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
})
@Getter
@Setter
public class CrmUser {

    @NotNull(message = "pole obowiązkowe")
    @Size(min = 1, message = "pole obowiązkowe")
    private String userName;

    @NotNull(message = "pole obowiązkowe")
    @Size(min = 1, message = "pole obowiązkowe")
    private String password;

    @NotNull(message = "pole obowiązkowe")
    @Size(min = 1, message = "pole obowiązkowe")
    private String matchingPassword;

    @NotNull(message = "pole obowiązkowe")
    @Size(min = 1, message = "pole obowiązkowe")
    private String firstName;

    @NotNull(message = "pole obowiązkowe")
    @Size(min = 1, message = "pole obowiązkowe")
    private String lastName;
}

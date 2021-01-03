package appdev.appdev.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;
        if(accountRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "Please Check email Address ");
        }
        if(accountRepository.existsByNickname(signUpForm.getEmail())){
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpForm.getNickname()}, "Please Check Nickname");
        }
    }
}

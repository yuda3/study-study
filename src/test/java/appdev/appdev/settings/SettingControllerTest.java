package appdev.appdev.settings;

import appdev.appdev.WithAccount;
import appdev.appdev.account.AccountRepository;
import appdev.appdev.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static appdev.appdev.settings.SettingsController.SETTINGS_PROFILE_URL;
import static appdev.appdev.settings.SettingsController.SETTINGS_PROFILE_VIEW_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SettingControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;


    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }


    @WithAccount("keesun")
    @DisplayName("Profile - normal")
    @Test
    void updateProfileForm() throws Exception {
        String bio = "Short";
        mockMvc.perform(get(SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }



    @WithAccount("keesun")
    @DisplayName("Profile - normal")
    @Test
    void updateProfile() throws Exception {
        String bio = "Short";
        mockMvc.perform(post(SETTINGS_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));
        Account keesun = accountRepository.findByNickname("keesun");
        assertEquals(bio, keesun.getBio());
    }

    @WithAccount("keesun")
    @DisplayName("Profile - error")
    @Test
    void updateProfile_withError() throws Exception {
        String bio = "ShortShortShortShortShortShortShortShortShortShortShortShortShortShortShortShortShort";
        mockMvc.perform(post(SETTINGS_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());
        Account keesun = accountRepository.findByNickname("keesun");
        assertNull(keesun.getBio());
    }

}

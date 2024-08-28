package com.campus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateUser {

	 @Autowired
	 private MockMvc mockMvc;
 
	    @Test
	    public void createUserWithDuplicatePhoneNumber_ShouldReturnBadRequest() throws Exception {
	        String userJson = "{ \"firstName\": \"Test\", \"lastName\": \"Test\", \"phoneNumber\": 9999999999, \"emailId\": \"test1@test.com\" }";
	        

	        mockMvc.perform(post("/automation-campus/create/user")
	                .header("roll-number", "1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(userJson))
	                .andExpect(status().isOk()); 
	        
	        mockMvc.perform(post("/automation-campus/create/user")
	                .header("roll-number", "1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(userJson))
	                .andExpect(status().isBadRequest()); 
	      
	    }

	    @Test
	    public void createUserWithoutRollNumber_ShouldReturnUnauthorized() throws Exception {
	        String userJson = "{ \"firstName\": \"Test\", \"lastName\": \"Test\", \"phoneNumber\": 8888888888, \"emailId\": \"test2@test.com\" }";
	        
	        mockMvc.perform(post("/automation-campus/create/user")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(userJson))
	                .andExpect(status().isUnauthorized()); 
	    }
	    @Test
	    public void createUserWithMissingFields_ShouldReturnBadRequest() throws Exception {
	        String userJson = "{ \"firstName\": \"\", \"lastName\": \"Test\", \"phoneNumber\": 8888888888, \"emailId\": \"test3@test.com\" }";
	        
	        mockMvc.perform(post("/automation-campus/create/user")
	                .header("roll-number", "1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(userJson))
	                .andExpect(status().isBadRequest()); 
	    }

	    @Test
	    public void createUserWithInvalidEmail_ShouldReturnBadRequest() throws Exception {
	        String userJson = "{ \"firstName\": \"Test\", \"lastName\": \"Test\", \"phoneNumber\": 7777777777, \"emailId\": \"invalid-email\" }";
	        
	        mockMvc.perform(post("/automation-campus/create/user")
	                .header("roll-number", "1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(userJson))
	                .andExpect(status().isBadRequest());
	    }


	    @Test
	    public void createUserWithValidData_ShouldReturnOk() throws Exception {
	        String userJson = "{ \"firstName\": \"Test\", \"lastName\": \"Test\", \"phoneNumber\": 6666666666, \"emailId\": \"test4@test.com\" }";
	        
	        mockMvc.perform(post("/automation-campus/create/user")
	                .header("roll-number", "1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(userJson))
	                .andExpect(status().isOk());
	    }

	 
	    @Test
	    public void createUserWithoutContentType_ShouldReturnUnsupportedMediaType() throws Exception {
	        String userJson = "{ \"firstName\": \"Test\", \"lastName\": \"Test\", \"phoneNumber\": 5555555555, \"emailId\": \"test5@test.com\" }";
	        
	        mockMvc.perform(post("/automation-campus/create/user")
	                .header("roll-number", "1")
	                .content(userJson))
	                .andExpect(status().isUnsupportedMediaType());
	    }

	  
	    @Test
	    public void createUserWithInvalidPhoneNumber_ShouldReturnBadRequest() throws Exception {
	        String userJson = "{ \"firstName\": \"Test\", \"lastName\": \"Test\", \"phoneNumber\": 123, \"emailId\": \"test6@test.com\" }";
	        
	        mockMvc.perform(post("/automation-campus/create/user")
	                .header("roll-number", "1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(userJson))
	                .andExpect(status().isBadRequest());
	    }
}
